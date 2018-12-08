package com.couchbase.financial.example.web.rest;

import com.couchbase.financial.example.domain.Customer;
import com.couchbase.financial.example.service.CustomerService;
import com.couchbase.financial.example.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@RestController
@RequestMapping("/api/customers")
@Slf4j
@RequiredArgsConstructor
public class CustomerResource {

    private final CustomerService service;

    @GetMapping("{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String id) {
        log.info("Request to get customer by id : {}", id);

        return ResponseUtil.wrapOrNotFound(service.findById(id));
    }
}
