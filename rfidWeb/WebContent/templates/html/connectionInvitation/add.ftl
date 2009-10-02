
${action.setPageType('safety_network_connections', 'add')!}

<head>
	<@n4.includeScript>
		var remoteTenantUrl = '<@s.url action="remoteOrgList" namespace="/ajax" />';
		document.observe("dom:loaded", function() {
				$('chooseConnection').hide();
				$('messageInput').hide();
				
				$('continueButton').observe('click', function(event) {
						event.stop();
						$('messageInput').show();
						$('chooseConnection').hide();
					});
				$('tenantSelect').observe('click', function(event) {
						event.stop();
						$('chooseTenant').hide();
						$('chooseConnection').show();
					});
				$('remoteTenant').observe('change', function(event) {
						var params = new Object();
						params[$('remoteTenant').name] = $('remoteTenant').getValue();
						getResponse(remoteTenantUrl, 'get', params); 
					});
			});
			
		
	</@n4.includeScript>
</head>
<#include "/templates/html/common/_orgPicker.ftl"/>
<@s.url id="cancelUrl" action="connections"/>
<@s.form action="connectionInvitationCreate" cssClass="fullForm contentBlock" theme="fieldid">
	
	<#include "../common/_formErrors.ftl"/>
	<div id="chooseTenant">
		<h2 class="clean"><@s.text name="lable.find_a_company"/></h2>
		<div class="singleColumn fluidSets">
			<div class="infoSet infoBlock">
				<label for="remoteTenantId" class="label"><@s.text name="label.access_code"/></label>
				<@s.select name="remoteTenantId" list="tenants" listKey="id" listValue="name" id="remoteTenant"/>
			</div>
		</div>
		<div class="actions">
			<@s.submit key="label.continue" id="tenantSelect"/> <@s.text name="label.or"/> <a href="${cancelUrl}"><@s.text name="label.cancel"/></a>
		</div>
	
	</div>
	
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
				<label for="localOrg" class="label"><@s.text name="label.to"/></label>
				<@n4.orgPicker name="localOrg" required="true" orgType="internal"/>
			</div>
					
			
		</div>
		
		<div class="actions">
			<@s.submit key="label.continue" id="continueButton"/> <@s.text name="label.or"/> <a href="${cancelUrl}"><@s.text name="label.cancel"/></a>
		</div>
	</div>
	
	
	<#include "../messages/_add.ftl"/>
	
	
</@s.form>