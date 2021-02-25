<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        ${msg("objectConfText")}
    <#elseif section = "form">
        <form id="auth-success-view-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">

                <div>
                    <label>You have provided wrong TAN</label>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                           type="submit" value="${msg("doSubmit")}"/>
                </div>

                <#--				Aditional fields to build POST request with body-->
                <#--                <input id="step" name="step" value="FINALIZED" hidden/>-->

            </div>
        </form>
    <#elseif section = "info" >
    </#if>
</@layout.registrationLayout>
