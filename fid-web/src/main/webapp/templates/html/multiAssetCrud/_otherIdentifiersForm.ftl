<@s.form id="step4form" cssClass="inputForm" theme="fieldidSimple">
    <#include '_identifiersList.ftl' />
</@s.form>

<div class="stepAction" id="step4Actions">

<@s.form action="assetMultiAddCreate" namespace="/" id="masterForm" theme="fieldid">
	<@s.submit id="saveButton" cssClass="save" name="save" key="hbutton.save" onclick="return mergeAndSubmit('step1form', 'step4form', 'masterForm');"/>
	<#if Session.sessionUser.hasAccess("createevent") >
		<@s.submit id="saveAndStartEventButton" cssClass="save" name="saveAndStartEvent" key="hbutton.saveandstartevent" onclick="return mergeAndSubmit('step1form', 'step4form', 'masterForm');"/>
	</#if>
	<@s.text name="label.or"/> <a href="#step3" onclick="backToStep3(); return false;"><@s.text name="label.back_to_step"/> 3</a>
</@s.form>

</div>
