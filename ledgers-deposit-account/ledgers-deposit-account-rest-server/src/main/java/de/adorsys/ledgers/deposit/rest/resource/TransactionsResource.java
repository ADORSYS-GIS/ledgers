/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.resource;

import de.adorsys.ledgers.deposit.api.domain.MockBookingDetailsBO;
import de.adorsys.ledgers.deposit.api.service.TransactionService;
import de.adorsys.ledgers.deposit.rest.annotation.DepositUserResource;
import de.adorsys.ledgers.deposit.rest.mapper.MockTransactionMapper;
import de.adorsys.ledgers.deposit.api.resource.TransactionsResourceAPI;
import de.adorsys.ledgers.deposit.api.domain.account.MockBookingDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@DepositUserResource
@RequiredArgsConstructor
@RequestMapping(TransactionsResourceAPI.BASE_PATH)
public class TransactionsResource implements TransactionsResourceAPI {
    private final TransactionService transactionService;
    private final MockTransactionMapper transactionMapper;


    @Override
    public ResponseEntity<Map<String, String>> transactions(List<MockBookingDetails> data) {
        List<MockBookingDetailsBO> dataBO = transactionMapper.toMockTransactionDetailsBO(data);
        return new ResponseEntity<>(transactionService.bookMockTransaction(dataBO), HttpStatus.CREATED);
    }
}