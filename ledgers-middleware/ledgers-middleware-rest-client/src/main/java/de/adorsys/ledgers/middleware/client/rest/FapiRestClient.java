package de.adorsys.ledgers.middleware.client.rest;

import de.adorsys.ledgers.middleware.rest.resource.FapiRestApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "fapiRestClient", url = LedgersURL.LEDGERS_URL, path = FapiRestApi.BASE_PATH)
public interface FapiRestClient extends FapiRestApi {
}
