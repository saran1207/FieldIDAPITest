<#macro columnClass package>
	<#if package.current>class="currentPackage"<#elseif package.preferred>class="preferred"</#if>
</#macro>
<title>Upgrade Account</title>
<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>
${action.setPageType('account_settings', 'show')!}
<#assign currentPackageFilter=action.currentPackageFilter()/>

<#include "/templates/html/common/_formErrors.ftl"/>
<table id="packages">
		<thead>
			<tr>
				<th></th>
				<#list packages as package>
					<th id="package_${package.name}" <@columnClass package/>>
					 	<#if package.current><@s.text name="label.your_current_package"/><br/></#if>
						<span class="packageName">${package.name?html}</span>
					</th>
				</#list>
			</tr>
		</thead>
		<tbody>
			<tr id="priceRow" class="nonFeatureSection">
				<td class="description"><@s.text name="label.price"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<@s.text name="label.per_user_per_month"><@s.param>${currentPackageFilter.getUpgradeContractForPackage(package).pricePerUserPerMonth?string.currency}</@s.param></@s.text>
					</td>
				</#list>
			</tr>
			
			<tr class="nonFeatureSection">
				<td class="description"><@s.text name="label.users"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<@s.text name="${action.labelForTenantLimit(package.users)}"/>
					</td>
				</#list>
			</tr>
			
			<tr class="nonFeatureSection">
				<td class="description"><@s.text name="label.max_assets"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<@s.text name="${action.labelForTenantLimit(package.assets)}"/>
					</td>
				</#list>
			</tr>
			
			<tr class="nonFeatureSection">
				<td class="description"><@s.text name="label.storage"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						${package.diskSpaceInMB} MB <@s.text name="label.total"/>
					</td>
				</#list>
			</tr>
			
			<tr class="nonFeatureSection">
				<td class="description"><@s.text name="label.free_trial"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if !package.free><@s.text name="label.yes"/></#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.rfid_enabled"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.mobile_access"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.safety_network_compatible"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.sechedule_email"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if package.includes('EmailAlerts') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.advanced_scheduling"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if package.includes('Projects') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.branded_satety_portal"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if package.includes('Branding') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>" />
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.partner_center"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if package.includes('PartnerCenter') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.third_party_integration"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if package.includes('AllowIntegration') >
							$
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.multi_location"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if package.includes('MultiLocation') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.custom_cert"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if package.includes('CustomCert') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.dedicated_program_manager"/></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if package.includes('DedicatedProgramManager') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr class="signUp">
				<td class="description"></td>
				<#list packages as package>
					<td <@columnClass package/>>
						<#if !(package.current)>
							<a href="<@s.url action="accountUpgrade" upgradePackageId="${package.name}"/>" id="upgradeTo_${package.name?html}"><@s.text name="label.upgrade"/></a>
						</#if>
					</td>
				</#list>
			</tr>
			
		</tbody>
		<tfoot>
			<tr>
				<td></td>
				<#list packages as package>
					<td <@columnClass package/>>
					</td>
				</#list>
			</tr>
		</tfoot>
			
	</table>