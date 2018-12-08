package com.couchbase.financial.example.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.financial.example.config.ApplicationProperties;
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

    private final ApplicationProperties properties;

    public CustomerService(Bucket bucket, ObjectMapper objectMapper, ApplicationProperties properties) {
        super(objectMapper, bucket, Customer.class);
        this.properties = properties;
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }

    public Collection<Customer> findByAddress(String address) {
        return getCouchbaseRepository().query(properties.getQuery().getFindCustomersByAddress(),
                Collections.singletonMap("address", address));
    }
}
