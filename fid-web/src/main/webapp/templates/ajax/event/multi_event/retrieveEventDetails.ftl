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

<#--WEB-2923 When updating some stuff into the DOM with <a> elements, IE prepends the URL of the current page to the value
  of the HREF attribute... this prevents the lightboxes from being found.. we need to clean out the href attributes that IE messes up by eliminating the junk before the # -->
$$('.recDefLightBox').each(function (e) {
    var oldHref = e.readAttribute('href');
    var fixedHref = oldHref.substring(oldHref.indexOf('#'), oldHref.length);
    e.writeAttribute('href', fixedHref);
});

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

