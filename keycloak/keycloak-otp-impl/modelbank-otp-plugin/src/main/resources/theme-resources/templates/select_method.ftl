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
							<li>${method_index + 1}. ${method.type}</li>
						</#list>
					</ul>
				</div>

				<div class="${properties.kcInputWrapperClass!}">
					"${javaString}"
				</div>
			</div>
		</form>
	<#elseif section = "info" >
		${msg("emailAuthInstruction")}
	</#if>
</@layout.registrationLayout>
