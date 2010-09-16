<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
	<script language="javascript" src="javascript/invite.js"> </script>
</head>

<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>

<@s.form action="sendInvite" cssClass="fullForm contentBlock" theme="fieldid">

<div class="safetyNetworkSplash">
	<h1 id="inbox_heading" class="safetyNetworkHeadings"><@s.text name="label.invite_a_company"/></h1>
	<div id="messageInput">
	
		<#include "../common/_formErrors.ftl"/>
	
		<div class="infoField">
			<label for="email" class="label inline"><@s.text name="label.to"/>: </label><span class="field_tip"><@s.text name="label.enter_single_addr"/></span>
			<@s.textfield name="email"/>
		</div>
	
		<div class="infoField">
			<label for="subject" class="label inline"><@s.text name="label.subject"/>: </label>
			<@s.textfield name="subject"/>
		</div>
		
		<div class="infoField">
			<label for="body" class="label inline"><@s.text name="label.body"/>: </label><span class="field_tip"><@s.text name="label.optional"/></span>
			<@s.textarea id="messageBody" name="body" onkeyup="updateExample();"/>
		</div>
		
		<div class="actions">
			<@s.submit key="label.sendinvitation" id="sendinvitation"/>
			<@s.text name="label.or"/>
			<a href="<@s.url action="safetyNetwork"/>"><@s.text name="label.cancel"/></a> 
		</div>
	</div>
	
	<div id="messageSample">
		<h4><@s.text name="label.sample_title"/></h4>
		<div class="sampleDesc"><@s.text name="label.sample_desc.1"/></div>
		<div class="sampleDesc"><@s.text name="label.sample_desc.2"/></div>
		<pre id="sampleMessage"></pre>
	</div>
	
	</@s.form>
	
	<script type="text/javascript">
		updateUrl = '<@s.url namespace="/ajax" action="invitationExample" />';
		updateExample();
	</script>
</div>