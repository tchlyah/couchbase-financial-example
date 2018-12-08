package com.couchbase.financial.example.domain;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Account {

    private String id;

    private String customerId;

    private Customer customer;

    private String name;

    private double amount;
}
