${action.setPageType('organization','add')!}

<#if limits.secondaryOrgsMaxed>
	<div class="limitWarning">
	<@s.text name="label.exceeded_your_org_limit">
		<@s.param>${limits.secondaryOrgsMax}</@s.param>
	</@s.text>
	</div>
<#else>
	<@s.form action="organizationCreate" theme="fieldid" cssClass="crudForm largeForm">
		<#include "_form.ftl"/>
		<div class="formAction">
			<@s.url id="cancelUrl" action="organizations"/>
			<@s.submit key="label.save"/>	
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" />	<@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</#if>