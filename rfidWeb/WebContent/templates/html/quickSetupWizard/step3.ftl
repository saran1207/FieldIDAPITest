<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>3</@s.param><@s.param>3</@s.param></@s.text></title>
<head>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
	
</head>
<div id="setupWizardStep3" class="setupWizardContent">
	<h2 class="clean"><@s.text name="label.import_industry_standard_equipment_templates"/></h2>
	
	<p><@s.text name="label.select_company_to_import_you_can_repeat_this_step"/></p>
	<@s.form action="step3Import" cssClass="fullForm fluentSets prominent" method="GET" theme="fieldid">	
		<table id="safetyNetworkConnections">
			<#list connections.list as connection >
				<#if action.hasAPublishedCatalog(connection.connectedOrg)>
					<tr>
						<td>
							<@s.radio name="uniqueID" list="%{getSingleMapElement(${connection.connectedOrg.tenant.id})}"  />
						</td>
						<td>
							<#assign tenant=connection.connectedOrg.tenant/>
							<#include "../common/_displayTenantLogo.ftl"/>
						</td>
						<td>
							<#if connection.connectedOrg.tenant.name == helper.configEntry("HOUSE_ACCOUNT_NAME")>
								<@s.text name="label.about_house_account_importing">
									<@s.param>${connection.connectedOrg.primaryOrg.name?html}</@s.param>
								</@s.text>
							<#else>
								${(connection.connectedOrg.primaryOrg.name?html)!}<br/>
								${(connection.connectedOrg.secondaryOrg.name?html)!}
							</#if>
						</td>
						<td class="alreadyImported">
							<#if action.isImportedFromConnection(connection)>
								<img src="<@s.url value="/images/small-check.png"/>" alt="<@s.text name="label.already_imported"/>"/>
							</#if>
						</td>
					</tr>
				</#if>
			</#list>
		</table>
		
		<div class="actions">
			<@s.submit key="label.next" />
			<@s.text name="label.or"/>
			<a href="<@s.url action="allDone"/>">
				<#if haveAnyImportsBeenCompleted>
					<@s.text name="label.im_done"/>
				<#else>
					<@s.text name="label.skip"/>
				</#if>
			</a>
		</div>
	</@s.form>
</div>