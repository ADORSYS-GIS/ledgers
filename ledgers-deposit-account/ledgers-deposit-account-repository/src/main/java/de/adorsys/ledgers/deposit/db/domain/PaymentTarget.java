/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.ledgers.deposit.db.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;


@Data
@Entity
@ToString(exclude = {"payment"})
public class PaymentTarget {
    @Id
    private String paymentId;

    private String endToEndIdentification;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "AMT"))
    @AttributeOverride(name = "currency", column = @Column(name = "CUR"))
    @Column(nullable = false)
    private Amount instructedAmount;

    @Embedded
    @AttributeOverride(name = "iban", column = @Column(name = "CRED_IBAN"))
    @AttributeOverride(name = "bban", column = @Column(name = "CRED_BBAN"))
    @AttributeOverride(name = "pan", column = @Column(name = "CRED_PAN"))
    @AttributeOverride(name = "maskedPan", column = @Column(name = "CRED_MASKED_PAN"))
    @AttributeOverride(name = "msisdn", column = @Column(name = "CRED_MSISDN"))
    @Column(nullable = false)
    private AccountReference creditorAccount;

    private String creditorAgent;

    private String creditorName;

    @Embedded
    @AttributeOverride(name = "street", column = @Column(name = "CRED_STREET"))
    @AttributeOverride(name = "buildingNumber", column = @Column(name = "CRED_BLD_NBR"))
    @AttributeOverride(name = "city", column = @Column(name = "CRED_CITY"))
    @AttributeOverride(name = "postalCode", column = @Column(name = "CRED_ZIP"))
    @AttributeOverride(name = "country", column = @Column(name = "CRED_CTRY"))
    @AttributeOverride(name = "line1", column = @Column(name = "line_1"))
    @AttributeOverride(name = "line2", column = @Column(name = "line_2"))
    private Address creditorAddress;
    @Enumerated(EnumType.STRING)
    private PurposeCode purposeCode;

    @Enumerated(EnumType.STRING)
    private ChargeBearer chargeBearer;

    private String remittanceInformationUnstructured;
    @Embedded
    private RemittanceInformationStructured remittanceInformationStructured;

    @ManyToOne(optional = false)
    private Payment payment;
}
