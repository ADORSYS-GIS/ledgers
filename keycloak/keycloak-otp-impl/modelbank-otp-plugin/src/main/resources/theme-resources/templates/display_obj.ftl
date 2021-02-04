<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        ${msg("objectConfText")}

        <form id="object-confirmation-form" class="${properties.kcFormClass!}" action="${postRequestUrl}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <input id="objId"
                       name="objId"
                       value="${context.objId}"
                       hidden/>
                <input id="authId"
                       name="authId"
                       value="${context.authId}"
                       hidden/>
                <input id="objType"
                       name="objType"
                       value="${context.objType}"
                       hidden/>
                <input id="step"
                       name="step"
                       value="CONFIRM_OBJ"
                       hidden/>

                <div>
                    <label>${object.description}</label>
                </div>
                <div>
                    <textarea disabled>${object.displayInfo}</textarea>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}"/>
                </div>

                <#--				Aditional fields to build POST request with body-->
                <#--input type="hidden" name="oprId" value="operation_id">
                <input type="hidden" name="externalId" value="external_id">
                <input type="hidden" name="authorisationId" value="auth_id">
                <input type="hidden" name="opType" value="${object.objType}">
                <input type="hidden" name="login" value="${auth.attemptedUsername}"-->

            </div>
        </form>
    </#if>
</@layout.registrationLayout>
