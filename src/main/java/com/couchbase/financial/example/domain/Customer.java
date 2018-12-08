package com.couchbase.financial.example.domain;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Customer {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private List<Address> addresses;

    @Data
    public static class Address {

        private String name;

        private String address;
    }

}
