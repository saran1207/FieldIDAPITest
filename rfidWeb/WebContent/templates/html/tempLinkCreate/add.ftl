
${action.setPageType('safety_network_connections', 'add')!}


<#include "/templates/html/common/_orgPicker.ftl"/>

<@s.form action="saveConnection" cssClass="fullForm contentBlock" theme="fieldid">
	<#include "../common/_formErrors.ftl"/>
	<@s.hidden name="remoteTenantId" />
	<@s.hidden name="connectionType" />
	
	<div class="singleColumn fluidSets">
		<div class="infoSet infoBlock">
			<label for="localOrg" class="label"><@s.text name="label.myorganization"/></label>
			<@n4.orgPicker name="localOrg" required="true" orgType="internal"/>
		</div>
				
		<div class="infoSet infoBlock">
			<label for="remoteOrg" class="label"><@s.text name="${connectionTypeLabel}"/></label>
			<@s.select name="remoteOrgId" list="remoteOrgs" listKey="id" listValue="name" />
		</div>
	</div>
	
	<div class="actions">
		<@s.submit key="label.save" id="saveButton"/> <@s.text name="label.or"/> <a href="<@s.url action="listConnections"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>