<#assign orgList>
	
	<#if !orgs.hasResults()>
		<div><@s.text name="label.search_found_no_orgs"/></div>
	<#else>
		<#list orgs.list as org>
			<div>
				<div><a href="#" org="${org.id}" orgName="${org.displayName?html}" >${org.name?html}</a></div>
				<div>${org.primaryOrg.name?html} ${(" | " + org.secondaryOrg.name?html)!} ${(" | " + org.customerOrg.name?html)!}${(" | " + org.divisionOrg.name?html)!} 
			</div>
		</#list>
		<div>
			${orgs.list?size} <@s.text name="label.of"/> ${orgs.totalResults} 
			<#if orgs.hasNextPage> <@s.label name="label.refine_your_search"/> </#if>
		</div>
	</#if>
</#assign>
$("orgPickerResults").update("${orgList?js_string}");

$$("#orgPickerResults a").each(function(element) { element.observe('click', updateOwner); });