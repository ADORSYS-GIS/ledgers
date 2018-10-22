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

package de.adorsys.ledgers.middleware.resource;

import de.adorsys.ledgers.middleware.exception.NotFoundRestException;
import de.adorsys.ledgers.middleware.service.MiddlewareService;
import de.adorsys.ledgers.middleware.service.domain.payment.PaymentResultTO;
import de.adorsys.ledgers.middleware.service.exception.PaymentNotFoundMiddlewareException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentResource {
    private static final Logger logger = LoggerFactory.getLogger(PaymentResource.class);

    private final MiddlewareService middlewareService;

    public PaymentResource(MiddlewareService middlewareService) {
        this.middlewareService = middlewareService;
    }

    @GetMapping("/{id}/status")
    public PaymentResultTO getPaymentStatusById(@PathVariable String id) {
        try {
            return middlewareService.getPaymentStatusById(id);
        } catch (PaymentNotFoundMiddlewareException e) {
            logger.error(e.getMessage(), e);
            throw new NotFoundRestException(e.getMessage());
        }
    }
}
