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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The following codes from the \"EventFrequency7Code\" of ISO 20022 are supported. - \"Daily\" - \"Weekly\" - \"EveryTwoWeeks\" - \"Monthly\" - \"EveryTwoMonths\" - \"Quarterly\" - \"SemiAnnual\" - \"Annual\"
 */
public enum FrequencyCode {

    DAILY("Daily"),

    WEEKLY("Weekly"),

    EVERYTWOWEEKS("EveryTwoWeeks"),

    MONTHLY("Monthly"),

    EVERYTWOMONTHS("EveryTwoMonths"),

    QUARTERLY("Quarterly"),

    SEMIANNUAL("SemiAnnual"),

    ANNUAL("Annual"),

    MONTHLYVARIABLE("Monthlyvariable");

    private String value;

    FrequencyCode(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FrequencyCode fromValue(String text) {
        for (FrequencyCode b : FrequencyCode.values()) {
            if (String.valueOf(b.value).equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }


}
