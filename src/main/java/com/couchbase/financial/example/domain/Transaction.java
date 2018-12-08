package com.couchbase.financial.example.domain;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Transaction {

    private String id;

    private String accountId;

    private Account account;

    private Double amount;

    private String counterparty;

    private String currency;

    private String description;

    private STATUS status;

    private String transactionType;

    public enum STATUS {
        SUCCESS,
        FAILED,
        CANCELED
    }
}
