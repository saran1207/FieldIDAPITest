<#assign html>
	<#include "/templates/html/event/multi_event/_form.ftl" />
	<div class="stepAction">
		<button id="continueButtonStep3"><@s.text name="label.continue"/></button>
		<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="backToStep(2);"><@s.text name="label.back_to_step"/> 2</a>
	</div>	
</#assign>


$('step3Loading').hide();
$('step3').update('${html?js_string}');
$('step3').show();

$('continueButtonStep3').observe("click", function(){	
	
	if (!$('ownerId').disabled && !$('ownerId').value){
		alert("Please select an owner");
	}else{	
		$('continueButtonStep3').disabled=true;
		var options = getStandardCallbacks();
		options.parameters = $('eventCreate').serialize();
		options.method =  "post";
		new Ajax.Request('<@s.url action="eventCheck" namespace="ajax"/>', options);
	}
});
						 
attachOrgEvents("#step3");

