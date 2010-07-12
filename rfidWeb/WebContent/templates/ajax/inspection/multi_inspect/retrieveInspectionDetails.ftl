<#assign html>
	<#include "/templates/html/inspection/multi_inspect/_form.ftl" />
	<div class="stepAction">
		<button id="continueButton"><@s.text name="label.continue"/></button>
		<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="backToStep(1)"><@s.text name="label.back_to_step"/> 1</a>
	</div>	
</#assign>


$('step2Loading').hide();
$('step2').update('${html?js_string}');
$('step2').show();
$('continueButton').observe("click", function(){	
		var options = getStandardCallbacks();
		options.parameters = $('inspectionCreate').serialize();
		options.method =  "post";
		new Ajax.Request('<@s.url action="inspectionCheck" namespace="ajax"/>', options);
});
						 
attachOrgEvents("#step2");

