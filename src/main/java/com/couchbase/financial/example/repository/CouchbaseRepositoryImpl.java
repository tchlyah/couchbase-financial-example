package com.couchbase.financial.example.repository;

import com.couchbase.client.core.BackpressureException;
import com.couchbase.client.core.CouchbaseException;
import com.couchbase.client.core.RequestCancelledException;
import com.couchbase.client.core.time.Delay;
import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.couchbase.client.java.error.TemporaryFailureException;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.util.retry.RetryBuilder;
import com.couchbase.client.java.util.retry.RetryWhenFunction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import rx.Observable;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.couchbase.client.java.query.consistency.ScanConsistency.STATEMENT_PLUS;
import static com.google.common.collect.ImmutableMap.of;
import static org.slf4j.LoggerFactory.getLogger;


@RequiredArgsConstructor
public class CouchbaseRepositoryImpl<E> implements CouchbaseRepository<E> {

    private static final Logger logger = getLogger(CouchbaseRepositoryImpl.class);

    @Getter
    private final ObjectMapper objectMapper;

    public static final String BUCKET_PARAM = "bucket";

    private final Bucket bucket;

    private final Class<E> entityClass;

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private static final RetryWhenFunction retryStrategy = retryStrategy();

    @Override
    public E save(String id, E entity) {
        logger.trace("Save entity '{}' with id '{}'", entity, id);
        try {
            String content = getObjectMapper().writeValueAsString(entity);
            runAsync(bucket -> bucket.upsert(RawJsonDocument.create(id, content)));
            return entity;
        } catch (JsonProcessingException e) {
            throw new CouchbaseException("Unable to save document with id " + id, e);
        }
    }

    @Override
    public void delete(String id) {
        logger.trace("Remove entity with id '{}'", id);
        try {
            runAsync(bucket -> bucket.remove(id));
        } catch (DocumentDoesNotExistException e) {
            logger.debug("Trying to delete document that does not exist : '{}'", id);
        }
    }

    @Override
    public E findOne(String id) {
        logger.trace("Find entity with id '{}'", id);
        RawJsonDocument document = runAsync(bucket -> bucket.get(id, RawJsonDocument.class));
        if (document == null) {
            return null;
        }
        try {
            return getObjectMapper().readValue(document.content(), entityClass);
        } catch (IOException e) {
            throw new CouchbaseException("Unable to read document with id " + id, e);
        }
    }

    @Override
    public void save(String id, String jsonContent) {
        logger.trace("Save document with id '{}' : \n'{}'", id, jsonContent);
        runAsync(bucket -> bucket.upsert(RawJsonDocument.create(id, jsonContent)));
    }

    @Override
    public Collection<E> query(String statement, Map<String, Object> params) {
        logger.debug("Execute n1ql request : \n{}", injectParameters(statement));
        try {
            AsyncN1qlQueryResult result = runAsync(bucket -> bucket
                    .query(createQuery(statement, params)));
            if (!result.parseSuccess()) {
                logger.error("Invalid N1QL request '{}' : {}", statement, single(result.errors()));
                throw new CouchbaseException("Invalid n1ql request");
            }

            return single(result.rows()
                    .map(this::mapToEntity)
                    .toList());
        } catch (Exception e) {
            throw new CouchbaseException("Unable to execute n1ql request", e);
        }
    }

    private ParameterizedN1qlQuery createQuery(String statement, Map<String, Object> params) {
        ParameterizedN1qlQuery parameterized = N1qlQuery.parameterized(injectParameters(statement), JsonObject.create(),
                N1qlParams.build().consistency(STATEMENT_PLUS));
        JsonObject statementParameters = (JsonObject) parameterized.statementParameters();
        params.forEach(statementParameters::put);
        return parameterized;
    }

    private E mapToEntity(AsyncN1qlQueryRow row) {
        try {
            return objectMapper.readValue(row.value().toString(), entityClass);
        } catch (IOException e) {
            throw new CouchbaseException("Unable to deserialize document", e);
        }
    }

    private String injectParameters(String statement) {
        return StrSubstitutor.replace(statement, of(BUCKET_PARAM, getBucketName()));
    }

    @Override
    public String getBucketName() {
        return bucket.name();
    }

    //<editor-fold desc="Helpers">
    @Nullable
    private <T> T single(Observable<T> observable) {
        return observable.toBlocking().singleOrDefault(null);
    }

    private <R> R runAsync(Function<AsyncBucket, Observable<R>> function) {
        return single(function.apply(bucket.async())
                .retryWhen(getRetryStrategy()));
    }

    @SuppressWarnings("unchecked")
    private static RetryWhenFunction retryStrategy() {
        return RetryBuilder
                .anyOf(TemporaryFailureException.class, RequestCancelledException.class, BackpressureException.class)
                .delay(Delay.exponential(TimeUnit.MILLISECONDS, 100))
                .max(3)
                .build();
    }
    //</editor-fold>
}
