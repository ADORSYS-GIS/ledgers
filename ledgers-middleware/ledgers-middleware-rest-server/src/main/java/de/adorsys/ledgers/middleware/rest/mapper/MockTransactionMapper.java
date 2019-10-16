package de.adorsys.ledgers.middleware.rest.mapper;

import de.adorsys.ledgers.deposit.api.domain.MockBookingDetailsBO;
import de.adorsys.ledgers.middleware.api.domain.account.MockBookingDetails;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MockTransactionMapper {

    List<MockBookingDetailsBO> toMockTransactionDetailsBO(List<MockBookingDetails> det);
}
