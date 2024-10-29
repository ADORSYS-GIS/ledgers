/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.client.rest;

import de.adorsys.ledgers.deposit.api.resource.*;
import org.springframework.cloud.openfeign.*;

@FeignClient(value = "ledgersAccount", url = LedgersURL.LEDGERS_URL, path=AccountRestAPI.BASE_PATH)
public interface AccountRestClient extends AccountRestAPI {}
