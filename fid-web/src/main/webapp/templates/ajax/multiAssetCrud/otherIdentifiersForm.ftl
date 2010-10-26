<#assign html>
	<#include "/templates/html/multiAssetCrud/_otherIdentifiersForm.ftl"/>
</#assign>

$('step4Loading').hide();
$('step4').update('${html?js_string}');
$('step4').show();
