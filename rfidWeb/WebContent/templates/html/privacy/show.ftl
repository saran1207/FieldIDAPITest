${action.setPageType('privacy', 'show')!}

<@s.form action="privacySettingsSave" cssClass="fullForm fluidSets" theme="fieldid" >
<h2 class="clean"><@s.text name="label.settings_and_privacy"/></h2>
<div class="infoSet">
	<label class="label"><@s.text name="label.autopublish" /></label>
	<@s.checkbox name="autoPublish" />
</div>
<div class="actions">
	<@s.submit key="hbutton.save" /> <@s.text name="label.or"/> <a href="<@s.url action="safetyNetwork"/>"><@s.text name="label.cancel"/></a>
</div>
</@s.form>
