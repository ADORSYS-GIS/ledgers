<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        ${msg("objectConfText")}
    <#elseif section = "form">
        <form id="auth-success-view-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">

                <div>
                    <label>${msgToUser}</label>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}"/>
                </div>

                <#--				Aditional fields to build POST request with body-->
                <input type="hidden" name="objId" value="${context.objId}">
                <input type="hidden" name="authId" value="${context.authId}">
                <input type="hidden" name="objType" value="${context.objType}">

                <input id="step" name="step" value="FINALIZED" hidden/>

            </div>
        </form>
    <#elseif section = "info" >
    </#if>
</@layout.registrationLayout>
