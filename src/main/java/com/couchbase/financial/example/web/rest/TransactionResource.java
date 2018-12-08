package com.couchbase.financial.example.web.rest;

import com.couchbase.financial.example.domain.Transaction;
import com.couchbase.financial.example.service.TransactionService;
import com.couchbase.financial.example.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;

/**
 * @author tayebchlyah
 * created on 08/12/2018
 */
@RestController
@RequestMapping("/api/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionResource {

    private final TransactionService service;

    @GetMapping("{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String id) {
        log.info("Request to get transaction by id : {}", id);

        return ResponseUtil.wrapOrNotFound(service.findById(id));
    }

    @GetMapping(params = {"fromDate", "toDate"})
    public Collection<Transaction> getByDates(
            @RequestParam(value = "fromDate") Instant fromDate,
            @RequestParam(value = "toDate") Instant toDate) {
        return service.findByDates(fromDate, toDate);
    }
}
