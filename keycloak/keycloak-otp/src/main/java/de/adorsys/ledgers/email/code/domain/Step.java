package de.adorsys.ledgers.email.code.domain;

import de.adorsys.ledgers.email.code.api.CmsConnector;
import de.adorsys.ledgers.email.code.api.CoreConnector;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.Response;
import java.util.List;

import static de.adorsys.ledgers.email.code.domain.ScaConstants.*;
import static de.adorsys.ledgers.email.code.domain.ScaStatus.*;

public enum Step {
    CONFIRM_OBJ {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context, CmsConnector cmsConnector, CoreConnector coreConnector) {
            //Get required data
            List<ScaMethod> scaMethods = coreConnector.getMethods();
            Object object = cmsConnector.getObject(holder);
            UserModel user = context.getUser();

            //UpdateCmsUser and Init Operation in Core
            cmsConnector.updateUserData(user);
            coreConnector.initObj(holder, object);

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
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context, CmsConnector cmsConnector, CoreConnector coreConnector) {
            String methodId = context.getHttpRequest().getDecodedFormParameters().getFirst("methodId");

            //StartSca and SelectMethod, update Cms with status
            coreConnector.selectMethod(holder, methodId);
            cmsConnector.setAuthorizationStatus(holder, ScaStatus.METHOD_SELECTED);
            context.challenge(context.form().setAttribute(REALM, context.getRealm()).createForm(CODE_INPUT));
        }
    },
    CODE_VALIDATION {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context, CmsConnector cmsConnector, CoreConnector coreConnector) {
            String code = context.getHttpRequest().getDecodedFormParameters().getFirst("code");
            boolean isValid = coreConnector.validateCode(holder, code);
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
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context, CmsConnector cmsConnector, CoreConnector coreConnector) {
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                                     context.form().createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
        }
    },
    FINALIZED {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context, CmsConnector cmsConnector, CoreConnector coreConnector) {
            coreConnector.execute(holder);
            cmsConnector.setAuthorizationStatus(holder, ScaStatus.FINALIZED);
            //TODO should update CMS.consentData with token!!! How???))
            context.success();
        }
    };

    public abstract void apply(ScaContextHolder holder, AuthenticationFlowContext context, CmsConnector cmsConnector, CoreConnector coreConnector);
}
