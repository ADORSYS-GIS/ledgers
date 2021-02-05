package de.adorsys.ledgers.middleware.rest.resource;

import de.adorsys.ledgers.middleware.api.domain.general.EmailTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.ledgers.middleware.api.domain.sca.GlobalScaResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.OpTypeTO;
import de.adorsys.ledgers.middleware.api.domain.sca.StartScaOprTO;
import de.adorsys.ledgers.middleware.api.domain.um.AisConsentTO;
import de.adorsys.ledgers.middleware.api.domain.um.ScaUserDataTO;
import de.adorsys.ledgers.middleware.api.service.MiddlewareFapiConsentService;
import de.adorsys.ledgers.middleware.api.service.MiddlewareFapiPaymentService;
import de.adorsys.ledgers.middleware.api.service.MiddlewareFapiScaService;
import de.adorsys.ledgers.middleware.api.service.MiddlewareUserManagementService;
import de.adorsys.ledgers.middleware.rest.annotation.MiddlewareResetResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@MiddlewareResetResource
@RequiredArgsConstructor
@RequestMapping(FapiRestApi.BASE_PATH)
public class FapiResource implements FapiRestApi {
    private final JavaMailSender sender;
    private final MiddlewareUserManagementService userManagementService;
    private final MiddlewareFapiPaymentService paymentService;
    private final MiddlewareFapiConsentService consentService;
    private final MiddlewareFapiScaService scaService;

    @Override
    public ResponseEntity<List<ScaUserDataTO>> getMethods(String login) {
        return ResponseEntity.ok(userManagementService.findByUserLogin(login).getScaUserData());
    }

    @Override
    public ResponseEntity<Void> initPayment(PaymentTO payment, String login) {
        paymentService.initiatePayment(payment, login);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> initCancel(String paymentId, String login) {
        paymentService.initiateCancellation(paymentId, login);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> initConsent(AisConsentTO consent, String login) {
        consentService.initConsent(consent, login);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<GlobalScaResponseTO> selectMethod(StartScaOprTO scaOpr, String methodId, String login) {
        return ResponseEntity.ok(scaService.startSca(scaOpr, methodId, login));
    }

    @Override
    public ResponseEntity<GlobalScaResponseTO> validateCode(String authId, String code, String login) {
        return ResponseEntity.ok(scaService.validateCode(authId, code, login));
    }

    @Override
    public ResponseEntity<Void> execute(String opId, OpTypeTO opType) {
        return ResponseEntity.ok().build(); //TODO STUB
    }

    @Override
    public ResponseEntity<Void> sendEmail(EmailTO message) {
        return send(message)
                       ? ResponseEntity.ok().build()
                       : ResponseEntity.badRequest().build();
    }

    private boolean send(EmailTO message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(message.getTo());
            simpleMailMessage.setFrom(message.getFrom());
            simpleMailMessage.setSubject(message.getSubject());
            simpleMailMessage.setText(message.getBody());
            sender.send(simpleMailMessage);
        } catch (MailException e) {
            return false;
        }
        return true;
    }
}
