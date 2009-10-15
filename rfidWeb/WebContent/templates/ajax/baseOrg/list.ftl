<#assign orgList>
	
	<#if !orgs.hasResults()>
		<div class="noResults"><@s.text name="label.search_found_no_orgs"/></div>
	<#else>
		<#list orgs.list as org>
			<div class="orgSearchResult">
				<div class="searchedName" ><a href="#" org="${org.id}" orgName="${org.displayName?html}" >${org.name?html}</a></div>
				<div class="heirarchy">
					${org.primaryOrg.name?html} ${(" | " + org.secondaryOrg.name?html)!} ${(" | " + org.customerOrg.name?html)!}${(" | " + org.divisionOrg.name?html)!}
				</div> 
			</div>
		</#list>
		<div class="resultCount">
			<@s.text name="label.showing"/> ${orgs.list?size} <@s.text name="label.of"/> ${orgs.totalResults} 
			<#if orgs.hasNextPage> <@s.text name="label.refine_your_search_if_organization_is_not_on_the_list"/> </#if>
		</div>
	</#if>
</#assign>
$("orgPickerResults").update("${orgList?js_string}");

$$("#orgPickerResults a").each(function(element) { element.observe('click', updateOwner); });
$('orgPickerLoading').hide();