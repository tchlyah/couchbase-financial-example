package com.couchbase.financial.example.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.financial.example.domain.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static com.google.common.collect.ImmutableMap.of;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@Service
public class TransactionService extends AbstractFinancialService<Transaction> {

    public static final String PREFIX = "transaction";

    private final AccountService accountService;

    public TransactionService(Bucket bucket, ObjectMapper objectMapper, AccountService accountService) {
        super(objectMapper, bucket, Transaction.class);
        this.accountService = accountService;
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return super.findById(id);
    }
}
