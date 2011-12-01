<@s.form id="step4form" cssClass="inputForm" theme="fieldidSimple">
    <#include '_identifiersList.ftl' />
</@s.form>

<div class="stepAction" id="step4Actions">

<@s.form action="assetMultiAddCreate" namespace="/" id="masterForm" theme="fieldid">
	<@s.submit id="saveButton" cssClass="save" name="save" key="hbutton.save"/>
	<#if Session.sessionUser.hasAccess("createevent") >
		<@s.submit id="saveAndStartEventButton" cssClass="save" name="saveAndStartEvent" key="hbutton.saveandstartevent" />
	</#if>
	<@s.text name="label.or"/> <a href="#step3" onclick="backToStep3(); return false;"><@s.text name="label.back_to_step"/> 3</a>
</@s.form>

</div>

<script type="text/javascript" >
	var buttons = new Array( 'saveButton', 'saveAndStartEventButton');
	var buttonLockMessages = new Array( '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />');
	var buttonMessages = new Array( '<@s.text name="hbutton.save" />', '<@s.text name="hbutton.saveandstartevent" />', '<@s.text name="hbutton.saveandprint" />');
	
	$$('#masterForm .save').each(function(element) {
			element.observe('click', function(event) {
				var element = Event.element(event);
				event.stop();
				mergeAndSubmit('step1form', 'step4form', 'masterForm');
			});
		});
</script>