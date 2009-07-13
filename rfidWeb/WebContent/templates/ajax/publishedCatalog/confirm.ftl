<#assign html>
	<#include "/templates/html/publishedCatalog/confirm.ftl"/>
</#assign>

$('step3Loading').hide();
$('step3').update('${html?js_string}');
$('step3').show();