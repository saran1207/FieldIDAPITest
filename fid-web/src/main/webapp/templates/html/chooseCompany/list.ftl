<head>
	<@n4.includeStyle href="chooseCompany" type="page"/>
</head>
<div id="tenantsContainer">
	<#if tenants.size() gt 1 >
		<div class="titleBlock">	
			<h1><@s.text name="title.site_address_found"/></h1>
			<p><@s.text name="instructions.found_multiple_site_addresses"/></p>
		</div>

		<ul id="tenantList">
			<li class="headingLabels">
				<div class="linkContainer"><label class="label"><@s.text name="label.click_to_login"/></label></div><label class="label"><@s.text name="label.click_to_bookmark"/></label>
			</li>
			<#list tenants as tenant>
				<li >
					<div class="linkContainer"><a href=#>${tenant.name}</a></div><input type="button" onclick="bookmark" value="<@s.text name="label.bookmark_company_button"><@s.param >${tenant.name}</@s.param></@s.text>" />
				</li>	
			</#list>
		</ul>
	<#else>
		
	</#if>
</div>