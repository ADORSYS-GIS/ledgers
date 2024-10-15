/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.mapper;

import de.adorsys.ledgers.deposit.api.domain.MockBookingDetailsBO;
import de.adorsys.ledgers.deposit.api.domain.account.MockBookingDetails;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MockTransactionMapper {

    List<MockBookingDetailsBO> toMockTransactionDetailsBO(List<MockBookingDetails> det);
}
