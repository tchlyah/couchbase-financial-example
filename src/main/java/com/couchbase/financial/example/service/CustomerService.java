package com.couchbase.financial.example.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.financial.example.domain.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@Service
public class CustomerService extends AbstractFinancialService<Customer> {

    public static final String PREFIX = "customer";

    public CustomerService(Bucket bucket, ObjectMapper objectMapper) {
        super(objectMapper, bucket, Customer.class);
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }
}
