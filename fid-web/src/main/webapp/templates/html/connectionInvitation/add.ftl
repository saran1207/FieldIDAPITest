<#assign currentAction="connectionInvitationCreate.action" />
<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>

<div id="safetyNetworkSplash">
	
	<@s.form action="connectionInvitationCreate" cssClass="fullForm" theme="fieldid">
	<@s.hidden id="remoteOrgId" name="remoteOrgId" value="${uniqueID}"/>
	<#assign organization=action.getRemoteOrg(uniqueID) />		
	<#assign orgTenant = organization.tenant>
	<@s.hidden id="remoteTenantId" name="remoteTenantId" value="${orgTenant.id}"/>


	
	<h1> <@s.text name="label.add"/>&nbsp;${organization.name}&nbsp;<@s.text name="label.as_a_lowercase"/>&nbsp;${connectionType?lower_case}&nbsp;<@s.text name="label.connection"/>?</h1>
	<div id="companyInfoContainer">
	
		<div id="inviteCompanyLogo" class="companyInvite">
		
			<#include "../common/_displayTenantLogo.ftl"/>
		</div>
		
		<div id="inviteCompanyInfo" class="companyInvite">
	    	${(organization.name?html)!}
	        <#if organization.webSite?exists>
	            <a href="${action.createHref(organization.webSite)}">${organization.webSite}</a>
	        </#if>
		</div>
	</div>
	
	<@s.url id="cancelUrl" action="safetyNetwork"/>
	<p id="invitationMessageHeading"><@s.text name="label.personal_message"/>&nbsp;<span class="italic"><@s.text name="label.optional"/></span></p>
	
		<#include "../common/_formErrors.ftl"/>

		<textarea id="invitationMessage" ></textarea>
		
		<div class="actions">
			<@s.submit key="label.submit" id="inviteButton"/> <@s.text name="label.or"/> <a style="display: inline;" href="${cancelUrl}"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>	
		
	
	<@s.url id="cancelUrl" action="safetyNetwork"/>
</div>
