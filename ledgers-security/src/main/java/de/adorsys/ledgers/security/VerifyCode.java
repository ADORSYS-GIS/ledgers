/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.security;

import lombok.*;//NOPMD

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCode {
    private String userId;
    private boolean verified;

    public VerifyCode(String userId) {
        this(userId, true);
    }
}
