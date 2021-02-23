<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        ${msg("objectConfText")}
    <#elseif section = "form">
        <form id="redirect-view-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
<#--                <input id="objId"-->
<#--                       name="objId"-->
<#--                       value="${object.id}"-->
<#--                       hidden/>-->
<#--                <input id="authId"-->
<#--                       name="authId"-->
<#--                       value="${context.authId}"-->
<#--                       hidden/>-->
<#--                <input id="objType"-->
<#--                       name="objType"-->
<#--                       value="${context.objType}"-->
<#--                       hidden/>-->
<#--                <input id="step"-->
<#--                       name="step"-->
<#--                       value="CONFIRM_OBJ"-->
<#--                       hidden/>-->

                <div>
                    <label>GREAT SUCCESS!</label>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}"/>
                </div>

            </div>
        </form>
    <#elseif section = "info" >
    </#if>
</@layout.registrationLayout>
