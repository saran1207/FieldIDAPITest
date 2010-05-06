<#assign html>
	<#include "/templates/html/common/_formErrors.ftl"/>
</#assign>

$('formErrors').replace('${html?js_string}');
$('formErrors').show();
$('formErrors').highlight();
