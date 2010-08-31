<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>
${action.setPageType('privacy', 'show')!}

<@s.form action="privacySettingsSave" cssClass="fullForm" theme="fieldid" >
	<h2 id="settings_privacy_heading" class="clean"><@s.text name="label.settings_and_privacy"/></h2>
	<div class="infoSet">
		<div class="desc">
			<h3><@s.text name="label.autopublish" /></h3>
			<p><@s.text name="label.autopublish_info" /></p>
		</div>
		<div class="check">
			<@s.checkbox id="chkAutoPublish" name="autoPublish" label="label.autopublish" />
			<label for="chkAutoPublish" class="label"><@s.text name="label.autopublish" /></label>
		</div>
	</div>
	
	<div class="infoSet">
		<div class="desc">
			<h3><@s.text name="label.autoaccept" /></h3>
			<p><@s.text name="label.autoaccept_info" /></p>
		</div>
		<div class="check">
			<@s.checkbox id="chkAutoAcceptConnections" name="autoAcceptConnections" />
			<label for="chkAutoAcceptConnections" class="label"><@s.text name="label.autoaccept" /></label>
		</div>
	</div>
	
	<div class="actions">
		<@s.submit key="hbutton.save" /> <@s.text name="label.or"/> <a href="<@s.url action="safetyNetwork"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>
