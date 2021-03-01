<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        ${msg("objectConfText")}
    <#elseif section = "form">
        <form id="auth-success-view-form" class="${properties.kcFormClass!}" action="${redirectUrl}" method="get">
            <div class="${properties.kcFormGroupClass!}">

                <div>
                    <label>${msgToUser}</label>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}"/>
                </div>

            </div>
        </form>
    <#elseif section = "info" >
    </#if>
</@layout.registrationLayout>
