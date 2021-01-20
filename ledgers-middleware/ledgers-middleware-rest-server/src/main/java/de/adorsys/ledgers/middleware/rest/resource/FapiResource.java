package de.adorsys.ledgers.middleware.rest.resource;

import de.adorsys.ledgers.middleware.api.domain.general.EmailTO;
import de.adorsys.ledgers.middleware.rest.annotation.MiddlewareResetResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MiddlewareResetResource
@RequiredArgsConstructor
@RequestMapping(FapiRestApi.BASE_PATH)
public class FapiResource implements FapiRestApi {
    private final JavaMailSender sender;

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
