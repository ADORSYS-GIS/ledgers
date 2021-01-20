package de.adorsys.ledgers.middleware.rest.resource;

import de.adorsys.ledgers.middleware.api.domain.general.EmailTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static de.adorsys.ledgers.middleware.rest.utils.Constants.UNPROTECTED_ENDPOINT;

@Tag(name = "LDG16 - FAPI endpoints")
public interface FapiRestApi {
    String BASE_PATH = "/fapi";

    @PostMapping("/email")
    @Operation(tags = UNPROTECTED_ENDPOINT, summary = "Send email")
    ResponseEntity<Void> sendEmail(@RequestBody EmailTO message);
}
