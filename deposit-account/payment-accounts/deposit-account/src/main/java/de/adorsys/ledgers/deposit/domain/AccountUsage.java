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

package de.adorsys.ledgers.deposit.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum AccountUsage {
    PRIV("PRIV"),
    ORGA("ORGA");

    private static final Map<String, AccountUsage> container = new HashMap<>();
    private String value;

    @JsonCreator
    private AccountUsage(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @JsonIgnore
    public static Optional<AccountUsage> getByValue(String value) {
        return Optional.ofNullable(container.get(value));
    }

    static {
        AccountUsage[] var0 = values();

        for (AccountUsage usageType : var0) {
            container.put(usageType.getValue(), usageType);
        }
    }
}
