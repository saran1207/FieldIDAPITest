
${action.setPageType('safety_network_connections', 'add')!}

<head>
	<@n4.includeScript src="connectionInvitations"/>
	<@n4.includeScript>
		var remoteTenantUrl = '<@s.url action="remoteOrgList" namespace="/ajax" />';
	</@n4.includeScript>
</head>
<#include "/templates/html/common/_orgPicker.ftl"/>
<@s.url id="cancelUrl" action="connections"/>

<div id="chooseTenant" >	
	<@s.form action="findRemoteOrg" namespace="/ajax" id="findRemoteOrg" cssClass="fullForm contentBlock" theme="fieldid">	
	
		<h2 class="clean"><@s.text name="label.find_a_company"/></h2>
		<div class="singleColumn fluidSets">
			<div class="infoSet infoBlock">
				<label for="remoteTenantId" class="label"><@s.text name="label.access_code"/></label>
				<span class="fieldHolder">
					<@s.textfield name="name" theme="fieldidSimple"/>
					<span class="errorMessage hide" id="remoteCompanyError">
						<@s.text name="error.company_does_not_exist"/>
					</span>
				</span>
				<@s.submit key="label.search"/>
			</div>
		</div>
		
		<div class="actions">
			<a href="${cancelUrl}"><@s.text name="label.cancel"/></a>
		</div>
	
	</@s.form>
</div>
	
<@s.form action="connectionInvitationCreate" cssClass="fullForm contentBlock" theme="fieldid">
	<@s.hidden id="remoteTenantId" name="remoteTenantId"/>
	<#include "../common/_formErrors.ftl"/>
	<div id="chooseConnection">
		<h2 class="clean"><@s.text name="label.choose_connections"/></h2>
		<div class="singleColumn fluidSets">
			<div class="infoSet infoBlock">
				<label for="remoteOrg" class="label"><@s.text name="label.add_other_company"/></label>
				<@s.select name="remoteOrgId" list="remoteOrgs" listKey="id" listValue="name" id="remoteOrgList" />
			</div>
			
			<div class="infoSet infoBlock">
				<label for="connectionType" class="label"><@s.text name="label.as_a"/></label>
				<span class="fieldHolder"><@s.radio name="connectionType" list="connectionTypes" listKey="id" listValue="name" /></span>
			</div>
			<div class="infoSet infoBlock">
				<label for="localOrg" class="label"><@s.text name="label.to_my_org"/></label>
				<@n4.orgPicker name="localOrg" required="true" orgType="internal" id="localOrgName"/>
			</div>
			
		</div>
		
		<div class="actions">
			<@s.submit key="label.continue" id="continueButton"/> <@s.text name="label.or"/> <a href="${cancelUrl}"><@s.text name="label.cancel"/></a>
		</div>
	</div>
	
	
	<#include "../messages/_add.ftl"/>
	
	
</@s.form>