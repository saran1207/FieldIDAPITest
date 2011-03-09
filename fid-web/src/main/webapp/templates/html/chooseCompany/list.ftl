<head>
	<@n4.includeStyle href="chooseCompany" type="page"/>
</head>
<div id="tenantsContainer">
	<div class="titleBlock">	
		<h1><@s.text name="title.site_address_found"/></h1>
		<#if tenants.size() gt 1 >
			<p class="actionInstructions"><@s.text name="instructions.found_multiple_site_addresses"/></p>
		<#else>
			<p class="actionInstructions"><@s.text name="instructions.found_one_site_address"/></p>
		</#if>
	</div>
	<ul id="tenantList">
		<li class="headingLabels">
			<div class="linkContainer"><label class="label"><@s.text name="label.click_to_login"/></label></div><label class="label"><@s.text name="label.click_to_bookmark"/></label>
		</li>
		<#list tenants as tenant>
			<li>
				<#assign loginUrl=action.getLoginUrlForTenant(tenant)/>
				<div class="linkContainer"><a href='${loginUrl}'>${tenant.name}</a></div><input type="button" onclick="createBookmark('${loginUrl}', 'Field ID - ${tenant.name}');" value="<@s.text name="label.bookmark_company_button"><@s.param >${tenant.name}</@s.param></@s.text>" />
			</li>	
		</#list>
	</ul>
</div>
