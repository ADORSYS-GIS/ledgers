package de.adorsys.ledgers.middleware.rest.resource;

import de.adorsys.ledgers.middleware.api.domain.general.EmailTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.ledgers.middleware.api.domain.sca.GlobalScaResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.OpTypeTO;
import de.adorsys.ledgers.middleware.api.domain.sca.StartScaOprTO;
import de.adorsys.ledgers.middleware.api.domain.um.AisConsentTO;
import de.adorsys.ledgers.middleware.api.domain.um.ScaUserDataTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.adorsys.ledgers.middleware.rest.utils.Constants.UNPROTECTED_ENDPOINT;

@Tag(name = "LDG16 - FAPI endpoints")
public interface FapiRestApi {
    String BASE_PATH = "/fapi";

    @GetMapping("/methods/{login}")
    @Operation(tags = UNPROTECTED_ENDPOINT, summary = "Retrieves List of Users Sca Methods")
    ResponseEntity<List<ScaUserDataTO>> getMethods(@PathVariable("login") String login);

    @PostMapping("/initiate/payment/{login}")
    @Operation(tags = UNPROTECTED_ENDPOINT, summary = "Initiates payment")
    ResponseEntity<Void> initPayment(@RequestBody PaymentTO payment, @PathVariable("login") String login);

    @PostMapping("/initiate/cancellation/{login}")
    @Operation(tags = UNPROTECTED_ENDPOINT, summary = "Initiates payment")
    ResponseEntity<Void> initCancel(@RequestParam("paymentId") String paymentId, @PathVariable("login") String login);

    @PostMapping("/initiate/consent/{login}")
    @Operation(tags = UNPROTECTED_ENDPOINT, summary = "Initiates payment")
    ResponseEntity<Void> initConsent(@RequestBody AisConsentTO consent, @PathVariable("login") String login);

    @PostMapping("/sca/select")
    @Operation(tags = UNPROTECTED_ENDPOINT, summary = "Initiate Sca Operation and chooses Sca Method")
    ResponseEntity<GlobalScaResponseTO> selectMethod(@RequestBody StartScaOprTO scaOpr,
                                                     @RequestParam(value = "methodId") String methodId,
                                                     @RequestParam(value = "login") String login);

    @PostMapping("/validate")
    @Operation(tags = UNPROTECTED_ENDPOINT, summary = "Validate code for Sca Operation")
    ResponseEntity<GlobalScaResponseTO> validateCode(@RequestParam(value = "authId") String authId,
                                                     @RequestParam(value = "code") String code,
                                                     @RequestParam(value = "login") String login);

    @PostMapping("/execute")
    ResponseEntity<Void> execute(@RequestParam(value = "opId") String opId,
                                 @RequestParam(value = "opType") OpTypeTO opType);

    @PostMapping("/email")
    @Operation(tags = UNPROTECTED_ENDPOINT, summary = "Send email")
    ResponseEntity<Void> sendEmail(@RequestBody EmailTO message);
}
