package de.adorsys.keycloak.otp.core.domain;


import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.CmsConnector;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.Response;
import java.util.List;

import static de.adorsys.keycloak.otp.core.domain.ScaStatus.EXEMPTED;
import static de.adorsys.keycloak.otp.core.domain.ScaStatus.IDENTIFIED;

/**
 * Describes the steps of SCA to complete the whole chain. Each step is represented by separate 'Authentication' in Keycloak admin console.
 *
 */
public enum Step {
    CONFIRM_OBJ {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            //Get required data
            UserModel user = context.getUser();
            List<ScaMethod> scaMethods = aspspConnector.getMethods(user);
            ConfirmationObject<Object> object = cmsConnector.getObject(holder);

            //UpdateCmsUser and Init Operation in Core
            cmsConnector.updateUserData(user);
            aspspConnector.initObj(holder, object, user.getUsername());

            //Pick corresponding ScaStatus and View to display
            ScaStatus status = scaMethods.isEmpty() ? EXEMPTED : IDENTIFIED;
            String viewToDisplay = scaMethods.isEmpty() ? ScaConstants.REDIRECT_VIEW : ScaConstants.SELECT_METHOD;
            cmsConnector.setAuthorizationStatus(holder, status);
            //TODO add methods to view
            context.challenge(context.form().setAttribute(ScaConstants.REALM, context.getRealm()).createForm(viewToDisplay));
        }
    },
    METHOD_SELECTED {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            String methodId = context.getHttpRequest().getDecodedFormParameters().getFirst("methodId");

            //StartSca and SelectMethod, update Cms with status
            aspspConnector.selectMethod(holder, methodId, context.getUser().getUsername());
            cmsConnector.setAuthorizationStatus(holder, ScaStatus.METHOD_SELECTED);
            context.challenge(context.form().setAttribute(ScaConstants.REALM, context.getRealm()).createForm(ScaConstants.CODE_INPUT));
        }
    },
    CODE_VALIDATION {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            String code = context.getHttpRequest().getDecodedFormParameters().getFirst("code");
            CodeValidationResult validateCode = aspspConnector.validateCode(holder, code, context.getUser().getUsername());
            if (validateCode.isValid()) {
                String msgToDisplayToUser = validateCode.isMultilevelScaRequired()
                                                    ? "Your authorization was successful, but you do not have enough rights to execute the operation, please ask other owners to confirm the operation."
                                                    : "Your authorization was successful, you can execute your operation.";
                //cmsConnector.setAuthorizationStatus(holder, VALIDATED);
                //pass msg to the view
                context.challenge(context.form().setAttribute(ScaConstants.REALM, context.getRealm()).createForm(ScaConstants.REDIRECT_VIEW));
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
            aspspConnector.execute(holder, context.getUser().getUsername());
            cmsConnector.setAuthorizationStatus(holder, ScaStatus.FINALIZED);
            //TODO should update CMS.consentData with token!!! How???)) Replace this 'null' with token.
            cmsConnector.pushToken(holder.getObjId(), null);
            context.success();
        }
    };

    public abstract void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                               CmsConnector cmsConnector, AspspConnector aspspConnector);
}
