package de.adorsys.ledgers.email.code.domain;

import de.adorsys.keycloak.connector.aspsp.api.AspspConnector;
import de.adorsys.keycloak.connector.cms.api.CmsConnector;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import de.adorsys.keycloak.otp.core.domain.ScaStatus;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.Response;
import java.util.List;

import static de.adorsys.ledgers.email.code.domain.ScaConstants.*;
import static de.adorsys.keycloak.otp.core.domain.ScaStatus.*;

public enum Step {
    CONFIRM_OBJ {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            //Get required data
            List<ScaMethod> scaMethods = aspspConnector.getMethods(null); //todo: set me
            Object object = cmsConnector.getObject(holder);
            UserModel user = context.getUser();

            //UpdateCmsUser and Init Operation in Core
            cmsConnector.updateUserData(user);
            aspspConnector.initObj(holder, object);

            //Pick corresponding ScaStatus and View to display
            ScaStatus status = scaMethods.isEmpty() ? EXEMPTED : IDENTIFIED;
            String viewToDisplay = scaMethods.isEmpty() ? REDIRECT_VIEW : SELECT_METHOD;
            cmsConnector.setAuthorizationStatus(holder, status);
            //TODO add methods to view
            context.challenge(context.form().setAttribute(REALM, context.getRealm()).createForm(viewToDisplay));
        }
    },
    METHOD_SELECTED {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            String methodId = context.getHttpRequest().getDecodedFormParameters().getFirst("methodId");

            //StartSca and SelectMethod, update Cms with status
            aspspConnector.selectMethod(holder, methodId);
            cmsConnector.setAuthorizationStatus(holder, ScaStatus.METHOD_SELECTED);
            context.challenge(context.form().setAttribute(REALM, context.getRealm()).createForm(CODE_INPUT));
        }
    },
    CODE_VALIDATION {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            String code = context.getHttpRequest().getDecodedFormParameters().getFirst("code");
            boolean isValid = aspspConnector.validateCode(holder, code);
            if (isValid) {
                cmsConnector.setAuthorizationStatus(holder, VALIDATED);
                context.challenge(context.form().setAttribute(REALM, context.getRealm()).createForm(REDIRECT_VIEW));
            } else {
                context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                                         context.form().createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
            }
        }
    },
    REJECTED {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                                     context.form().createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
        }
    },
    FINALIZED {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            aspspConnector.execute(holder);
            cmsConnector.setAuthorizationStatus(holder, ScaStatus.FINALIZED);
            //TODO should update CMS.consentData with token!!! How???))
            context.success();
        }
    };

    public abstract void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                               CmsConnector cmsConnector, AspspConnector aspspConnector);
}
