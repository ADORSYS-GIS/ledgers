package de.adorsys.ledgers.deposit.api.domain;

import lombok.Data;

@Data
public class RemittanceInformationStructuredBO {
    private String reference;
    private String referenceType;
    private String referenceIssuer;
}
