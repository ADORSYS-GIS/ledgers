package de.adorsys.ledgers.middleware.rest.exception;

import de.adorsys.ledgers.deposit.api.exception.DepositErrorCode;
import org.springframework.http.HttpStatus;

import java.util.EnumMap;

import static de.adorsys.ledgers.deposit.api.exception.DepositErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class DepositHttpStatusResolver {
    private static final EnumMap<DepositErrorCode, HttpStatus> container = new EnumMap<>(DepositErrorCode.class);

    private DepositHttpStatusResolver() {
    }

    static {
        //404 Block
        container.put(DEPOSIT_ACCOUNT_NOT_FOUND, NOT_FOUND);
        container.put(PAYMENT_NOT_FOUND, NOT_FOUND);
        container.put(TRANSACTION_NOT_FOUND, NOT_FOUND);

        //400 Block
        container.put(INSUFFICIENT_FUNDS, BAD_REQUEST);
        container.put(DEPOSIT_ACCOUNT_EXISTS, BAD_REQUEST);
        container.put(PAYMENT_PROCESSING_FAILURE, BAD_REQUEST);
        container.put(PAYMENT_WITH_ID_EXISTS, BAD_REQUEST);
        container.put(DEPOSIT_OPERATION_FAILURE, BAD_REQUEST);
        container.put(ACCOUNT_BLOCKED_DELETED, BAD_REQUEST);
    }

    public static HttpStatus resolveHttpStatusByCode(DepositErrorCode code) {
        return container.get(code);
    }
}
