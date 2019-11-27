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

package de.adorsys.ledgers.middleware.impl.test;

import de.adorsys.ledgers.deposit.api.client.ExchangeRateClient;
import de.adorsys.ledgers.deposit.api.service.EnableDepositAccountService;
import de.adorsys.ledgers.middleware.impl.EnableLedgersMiddlewareService;
import de.adorsys.ledgers.postings.impl.EnablePostingService;
import de.adorsys.ledgers.sca.service.EnableSCAService;
import de.adorsys.ledgers.um.impl.EnableUserManagementService;
import de.adorsys.ledgers.util.EnableUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableLedgersMiddlewareService
@EnableDepositAccountService
@EnableUserManagementService
@EnableSCAService
@EnablePostingService
@EnableUtils
@EnableFeignClients(basePackageClasses = ExchangeRateClient.class)
public class MiddlewareServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MiddlewareServiceApplication.class).run(args);
    }
}
