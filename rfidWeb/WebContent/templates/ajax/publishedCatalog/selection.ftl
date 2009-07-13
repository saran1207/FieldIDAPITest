<#assign html>
	<#include "/templates/html/publishedCatalog/importForm.ftl"/>
</#assign>

$('step2Loading').hide();
$('step2').update('${html?js_string}');
$('step2').show();