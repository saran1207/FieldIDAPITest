<#assign orgList>
	<#list orgs as org>
		<div><a href="#" org="${org.id}" orgName="${org.displayName?html}" >${org.displayName?html}</a></div>
	</#list>
</#assign>
$("orgPickerResults").update("${orgList?js_string}");

$$("#orgPickerResults a").each(function(element) { element.observe('click', updateOwner); });