
<#assign html>
	<#include "/templates/html/event/multi_event/_selectEventSchedule.ftl" />
	<div class="stepAction">
		<button id="continueButton"><@s.text name="label.continue"/></button>
		<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="backToStep(1)"><@s.text name="label.back_to_step"/> 1</a>
	</div>	
</#assign>


$('step2Loading').hide();
$('step2').update('${html?js_string}');
$('step2').show();
$('continueButton').observe("click", function(){	
	$('retrieveEventDetails').request(getStandardCallbacks());
		toStep(3, "step3Loading");
});
						 
attachOrgEvents("#step2");

