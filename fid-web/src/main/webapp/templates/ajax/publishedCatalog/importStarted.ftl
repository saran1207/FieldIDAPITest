<#assign html>
	<#include "/templates/html/publishedCatalog/importStarted.ftl"/>
</#assign>
$('step4Loading').hide();
$('step4').update('${html?js_string}');
$('step4').show();

if (importDoneName != undefined) {
	$('importDone').value = importDoneName;
}