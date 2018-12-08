package com.couchbase.financial.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Query query = new Query();

    @Data
    public static class Query {

        private String findCustomersByAddress;

        private String findTransactionsByDates;
    }
}
