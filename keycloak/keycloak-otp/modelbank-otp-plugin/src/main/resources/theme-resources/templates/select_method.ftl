<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        ${msg("methodsTitleText",realm.displayName)}
    <#elseif section = "form">
        <form id="select-sca-method-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcLabelWrapperClass!}">
                    <label for="methods" class="${properties.kcLabelClass!}">${msg("methodsAvailableLabel")}</label>
                </div>

                <div>
                    <ul>
                        <#list scaMethods as method>
                            <input type="radio" name="methodId" value="${method.id}">
                            <label for="${method.type}">${method.type}</label>
                            <br>
                        </#list>
                    </ul>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                           type="submit" value="${msg("doSubmit")}"/>
                </div>

                <#--				Aditional fields to build POST request with body-->
                <input type="hidden" name="step" value="METHOD_SELECTED">

                <input type="hidden" name="oprId" value="operation_id">
                <input type="hidden" name="objId" value="object_id">
                <input type="hidden" name="authId" value="auth_id">
                <input type="hidden" name="objType" value="PAYMENT">
                <input type="hidden" name="login" value="${auth.attemptedUsername}">

            </div>
        </form>
    <#elseif section = "info" >
    </#if>
</@layout.registrationLayout>
