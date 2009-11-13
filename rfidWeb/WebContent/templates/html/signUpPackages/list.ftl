<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>
<title><@s.text name="title.sign_up_for_an_account"/></title>

<div id="mainContent" class="fullScreen">
	<h1><@s.text name="title.sign_up_for_an_account"/></h1>
	<table id="packages">
		<thead>
			<tr>
				<th></th>
				<#list packages as package>
					<th id="package_${package.name}" <#if package.preferred>class="preferred"</#if>>
						${package.name?html}
					</th>
				</#list>
			</tr>
		</thead>
		<tbody>
			<tr id="priceRow" class="nonFeatureSection">
				<td class="description"><@s.text name="label.price"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						${package.defaultPricePerUserPerMonth?string.currency}
					</td>
				</#list>
			</tr>
			
			<tr class="nonFeatureSection">
				<td class="description"><@s.text name="label.users"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<@s.text name="${action.labelForTenantLimit(package.users)}"/>
					</td>
				</#list>
			</tr>
			
			<tr class="nonFeatureSection">
				<td class="description"><@s.text name="label.max_assets"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<@s.text name="${action.labelForTenantLimit(package.assets)}"/>
					</td>
				</#list>
			</tr>
			
			<tr class="nonFeatureSection">
				<td class="description"><@s.text name="label.storage"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						${package.diskSpaceInMB} MB <@s.text name="label.total"/>
					</td>
				</#list>
			</tr>
			
			<tr class="nonFeatureSection">
				<td class="description"><@s.text name="label.free_trial"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if !package.free><@s.text name="label.yes"/></#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.rfid_enabled"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.mobile_access"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.safety_network_compatible"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.sechedule_email"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if package.includes('EmailAlerts') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.advanced_scheduling"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if package.includes('Projects') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.branded_satety_portal"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if package.includes('Branding') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>" />
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.partner_center"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if package.includes('PartnerCenter') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.third_party_integration"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if package.includes('AllowIntegration') >
							$
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.multi_location"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if package.includes('MultiLocation') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.custom_cert"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if package.includes('CustomCert') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.dedicated_program_manager"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<#if package.includes('DedicatedProgramManager') >
							<img src="<@s.url value="/images/icon_check.gif"/>" alt="<@s.text name="label.x"/>"/>
						</#if>
					</td>
				</#list>
			</tr>
			
			<tr class="signUp">
				<td class="description"></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<a href="<@s.url action="signUpAdd" signUpPackageId="${package.name}"/>"><@s.text name="label.sign_up_now"/></a>
					</td>
				</#list>
			</tr>
			
		</tbody>
		<tfoot>
			<tr>
				<td></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
					</td>
				</#list>
			</tr>
		</tfoot>
			
	</table>
	<div>
		<@s.text name="label.have_an_account_already"/> <a href="<@s.url action="login" namespace="/"/>"><@s.text name="label.return_to_sign_in"/></a>
	</div>
</div>