package com.couchbase.financial.example.web.rest;

import com.couchbase.financial.example.domain.Account;
import com.couchbase.financial.example.domain.Customer;
import com.couchbase.financial.example.service.AccountService;
import com.couchbase.financial.example.service.CustomerService;
import com.couchbase.financial.example.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@RestController
@RequestMapping("/api/accounts")
@Slf4j
@RequiredArgsConstructor
public class AccountResource {

    private final AccountService service;

    @GetMapping("{id}")
    public ResponseEntity<Account> getAccount(@PathVariable String id) {
        log.info("Request to get account by id : {}", id);

        return ResponseUtil.wrapOrNotFound(service.findById(id));
    }
}
