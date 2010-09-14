<#assign currentAction="connectionInvitationAdd.action" />
<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>

<div id="safetyNetworkSplash">
	
	<@s.form id="connectionInvitationCreate" action="connectionInvitationCreate" cssClass="fullForm" theme="fieldid">
	<@s.hidden id="uniqueID" name="uniqueID" value=uniqueID/>
	<@s.hidden id="remoteOrgId" name="remoteOrgId" value=uniqueID/>
	<@s.hidden id="searchText" name="searchText" value=searchText/>
	<#assign organization = action.getRemoteOrg(uniqueID) />		
	<@s.hidden id="remoteTenantId" name="remoteTenantId" value="${organization.tenant.id}"/>
	
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
		<@s.textarea  id="invitationMessage" name="personalizedBody"/>
		
		<div class="actions">
			<@s.url id="cancelUrl" action="findConnections" searchText="${searchText}"/>
			<@s.submit key="label.send_request" id="inviteButton"/> <@s.text name="label.or"/> <a href="#" onclick="redirect('${cancelUrl}'); return false;"/><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>	
		
	
	<@s.url id="cancelUrl" action="safetyNetwork"/>
</div>
