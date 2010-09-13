<#assign currentAction="connectionInvitationAdd.action" />
<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>

<#assign organization=action.getRemoteOrg(request.getParameter("uniqueID"))/>
<#assign connectionType=request.getParameter("type")/>
<div id="safetyNetworkSplash">
	<h1> <@s.text name="label.add"/>&nbsp;${organization.name}&nbsp;<@s.text name="label.as_a_lowercase"/>&nbsp;${connectionType}&nbsp;<@s.text name="label.connection"/>?</h1>
	
	<div id="companyInfoContainer">
	
		<div id="inviteCompanyLogo" class="companyInvite">
			<#assign tenant = organization.tenant>
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
	<br/>
	<p id="invitationMessageHeading"><@s.text name="label.personal_message"/>&nbsp;<span class="italic"><@s.text name="label.optional"/></span></p>
	<@s.form id="connectionInvitationCreate" action="connectionInvitationCreate" cssClass="fullForm" theme="fieldid">
	
		<textarea id="invitationMessage" ></textarea>
	
		<@s.hidden name="connectionType" value=connectionType/>
		
		<div class="actions">
			<@s.submit key="label.submit" id="inviteButton"/> <@s.text name="label.or"/> <a style="display: inline;" href="${cancelUrl}"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>	
		
	
	<@s.url id="cancelUrl" action="safetyNetwork"/>
</div>
