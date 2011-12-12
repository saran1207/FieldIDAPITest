<#assign html>
	<#include "/templates/html/common/_formErrors.ftl"/>
</#assign>
var formError = $('formErrors');
formError.replace('${html?js_string}');
formError.show();
formError.scrollTo();
formError.highlight();
$('continueButtonStep3').disabled = false;
