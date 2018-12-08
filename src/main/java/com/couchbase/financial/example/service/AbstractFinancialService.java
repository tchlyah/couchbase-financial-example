package com.couchbase.financial.example.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.financial.example.repository.CouchbaseRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Optional;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
public abstract class AbstractFinancialService<E> {

    public static final String SEPARATOR = "_";

    @Getter
    private final CouchbaseRepositoryImpl<E> couchbaseRepository;

    public AbstractFinancialService(ObjectMapper objectMapper, Bucket bucket, Class<E> clazz) {
        this.couchbaseRepository = new CouchbaseRepositoryImpl<>(objectMapper, bucket, clazz);
    }

    public Optional<E> findById(String id) {
        return Optional.ofNullable(couchbaseRepository.findOne(getPrefix() + SEPARATOR + id));
    }

    protected abstract String getPrefix();
}
