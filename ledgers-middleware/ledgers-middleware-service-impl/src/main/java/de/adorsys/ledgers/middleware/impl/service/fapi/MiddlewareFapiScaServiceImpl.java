package de.adorsys.ledgers.middleware.impl.service.fapi;

import de.adorsys.ledgers.deposit.api.service.DepositAccountPaymentService;
import de.adorsys.ledgers.middleware.api.domain.sca.GlobalScaResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.StartScaOprTO;
import de.adorsys.ledgers.middleware.api.exception.MiddlewareErrorCode;
import de.adorsys.ledgers.middleware.api.exception.MiddlewareModuleException;
import de.adorsys.ledgers.middleware.api.service.MiddlewareFapiScaService;
import de.adorsys.ledgers.middleware.impl.converter.ScaResponseConverter;
import de.adorsys.ledgers.middleware.impl.service.ScaResponseMessageResolver;
import de.adorsys.ledgers.sca.domain.*;
import de.adorsys.ledgers.sca.service.SCAOperationService;
import de.adorsys.ledgers.um.api.domain.UserBO;
import de.adorsys.ledgers.um.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static de.adorsys.ledgers.sca.domain.OpTypeBO.CONSENT;
import static de.adorsys.ledgers.sca.domain.OpTypeBO.valueOf;

@Service
@RequiredArgsConstructor
public class MiddlewareFapiScaServiceImpl implements MiddlewareFapiScaService {
    private static final String NO_USER_MESSAGE = "No user message";
    @Value("${ledgers.sca.authCode.validity.seconds:180}")
    private int authCodeLifetime;

    private final UserService userService;
    private final DepositAccountPaymentService paymentService;
    private final SCAOperationService scaOperationService;
    private final ScaResponseMessageResolver messageResolver;
    private final ScaResponseConverter scaResponseConverter;

    @Override
    public GlobalScaResponseTO startSca(StartScaOprTO scaOpr, String methodId, String login) {
        UserBO user = userService.findByLogin(login);
        if (!user.hasSCA()) {
            throw MiddlewareModuleException.builder()
                          .errorCode(MiddlewareErrorCode.SCA_UNAVAILABLE)
                          .devMsg("Sorry, but do not have any SCA Methods configured!")
                          .build();
        }
        OpTypeBO opTypeBO = valueOf(scaOpr.getOpType().name());
        int scaWeight = resolveWeightForOperation(opTypeBO, scaOpr.getOprId(), user);
        AuthCodeDataBO codeData = new AuthCodeDataBO(user.getLogin(), null, scaOpr.getOprId(), null, NO_USER_MESSAGE,
                                                     authCodeLifetime, opTypeBO, scaOpr.getAuthorisationId(), scaWeight);
        SCAOperationBO operation = scaOperationService.checkIfExistsOrNew(codeData);

        String psuMessage = "";
        AuthCodeDataBO data = new AuthCodeDataBO(user.getLogin(), methodId, operation.getOpId(), null, psuMessage, authCodeLifetime, operation.getOpType(), operation.getId(), scaWeight);
        operation = scaOperationService.generateAuthCode(data, user, ScaStatusBO.SCAMETHODSELECTED);
        return scaResponseConverter.mapResponse(operation, user.getScaUserData(), messageResolver.updateMessage(psuMessage, operation), null, scaWeight, null);
    }

    @Override
    public GlobalScaResponseTO validateCode(String authId, String code, String login) {
        SCAOperationBO operation = scaOperationService.loadAuthCode(authId);
        UserBO user = userService.findByLogin(login);

        int scaWeight = resolveWeightForOperation(operation.getOpType(), operation.getOpId(), user);
        String psuMessage = messageResolver.getTemplate(operation);
        ScaValidationBO scaValidation = scaOperationService.validateAuthCode(operation.getId(), operation.getOpId(), code, scaWeight);
        operation.setScaStatus(scaValidation.getScaStatus());

        return scaResponseConverter.mapResponse(operation, user.getScaUserData(), messageResolver.updateMessage(psuMessage, operation), null, scaWeight, scaValidation.getAuthConfirmationCode());
    }

    private int resolveWeightForOperation(OpTypeBO opType, String oprId, UserBO user) {
        return opType == CONSENT
                       ? user.resolveMinimalWeightForIbanSet(userService.loadConsent(oprId).getUniqueIbans())
                       : user.resolveWeightForAccount(paymentService.getPaymentById(oprId).getAccountId());
    }
}
