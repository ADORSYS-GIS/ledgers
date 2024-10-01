/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.exception;

import de.adorsys.ledgers.util.exception.DepositErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;


@Slf4j
class HttpStatusResolverTest {
    @Test
    void testDepositStatusResolverCoverage() {
        List<DepositErrorCode> codes = Arrays.asList(DepositErrorCode.values());
        codes.forEach(c -> {
            boolean isNull = DepositHttpStatusResolver.resolveHttpStatusByCode(c) == null;
            if (isNull) {
                log.error("{} is missing in Resolver", c.name());
            }
            assertFalse(isNull);
        });
    }

}
