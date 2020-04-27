package de.adorsys.ledgers.deposit.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "CurrencyExchangeRates", url = ExchangeRateClient.CURRENCY_URL)
public interface ExchangeRateClient {
    String CURRENCY_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    @GetMapping
    ResponseEntity<String> getRatesToEur();
}
