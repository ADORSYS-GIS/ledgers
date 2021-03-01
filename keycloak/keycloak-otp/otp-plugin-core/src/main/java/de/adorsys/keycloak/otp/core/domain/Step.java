package de.adorsys.keycloak.otp.core.domain;


import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.CmsConnector;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserModel;

import java.util.List;

import static de.adorsys.keycloak.otp.core.domain.ScaStatus.EXEMPTED;
import static de.adorsys.keycloak.otp.core.domain.ScaStatus.IDENTIFIED;

/**
 * Describes the steps of SCA to complete the whole chain. Each step is represented by separate 'Authentication' in Keycloak admin console.
 */
public enum Step {
    CONFIRM_OBJ {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            //Get required data
            UserModel user = context.getUser();
            List<ScaMethod> scaMethods = aspspConnector.getMethods(user);
            ConfirmationObject<Object> confirmationObject = cmsConnector.getObject(holder);

            //UpdateCmsUser and Init Operation in Core
            cmsConnector.updateUserData(user);
            aspspConnector.initObj(holder, confirmationObject, user.getUsername());

            //Pick corresponding ScaStatus and View to display
            ScaStatus status = scaMethods.isEmpty() ? EXEMPTED : IDENTIFIED;
            String viewToDisplay = scaMethods.isEmpty() ? ScaConstants.REDIRECT_VIEW : ScaConstants.SELECT_METHOD;
            cmsConnector.setAuthorizationStatus(holder, status);

            if (ScaConstants.SELECT_METHOD.equals(viewToDisplay)) {
                holder.setObjId(confirmationObject.getId());

                context.challenge(context.form()
                                          .setAttribute(ScaConstants.REALM, context.getRealm())
                                          .setAttribute("scaMethods", scaMethods.toArray())
                                          .setAttribute("context", holder)
                                          .createForm(viewToDisplay));
            } else {
                context.challenge(context.form()
                                          .setAttribute(ScaConstants.REALM, context.getRealm())
                                          .createForm(viewToDisplay));
            }

        }
    },
    METHOD_SELECTED {
        @Override
        public void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                          CmsConnector cmsConnector, AspspConnector aspspConnector) {
            String methodId = context.getHttpRequest().getDecodedFormParameters().getFirst("methodId");

            // StartSca and SelectMethod, update CMS with status
            String psuMessage = aspspConnector.selectMethod(holder, methodId, context.getUser().getUsername());
            cmsConnector.setAuthorizationStatus(holder, ScaStatus.METHOD_SELECTED);
            context.challenge(context.form()
                                      .setAttribute(ScaConstants.REALM, context.getRealm())
                                      .setAttribute("context", holder)
                                      .createForm(ScaConstants.CODE_INPUT));
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
                                                    : "Your authorization was successful, you are going to be redirected to TPP.";

                cmsConnector.setAuthorizationStatus(holder, ScaStatus.FINALIZED);

                // TODO: obtain real session code from keycloak.
                String sessionCode = "1234525245";

                String redirectUrl = "https://www.google.com/?code=" + sessionCode;

                context.challenge(context.form()
                                          .setAttribute(ScaConstants.REALM, context.getRealm())
                                          .setAttribute("msgToUser", msgToDisplayToUser)
                                          .setAttribute("redirectUrl", redirectUrl)
                                          .setAttribute("context", holder)
                                          .createForm(ScaConstants.SUCCESS_VIEW));
            } else {

                // TODO: should we set FAILED status? Should we count wrong TAN attempts?
                context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                                         context.form()
                                                 .createForm(ScaConstants.FAILURE_VIEW));
            }
        }
    };

    public abstract void apply(ScaContextHolder holder, AuthenticationFlowContext context,
                               CmsConnector cmsConnector, AspspConnector aspspConnector);

}
