package com.couchbase.financial.example.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.financial.example.domain.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@Service
public class AccountService extends AbstractFinancialService<Account> {

    public static final String PREFIX = "account";

    private final CustomerService customerService;

    public AccountService(ObjectMapper objectMapper, CustomerService customerService, Bucket bucket) {
    super(objectMapper, bucket, Account.class);
        this.customerService = customerService;
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }

    @Override
    public Optional<Account> findById(String id) {
        return super.findById(id);
    }
}
