<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>
<div id="safetyNetworkSplash" class="safetyNetworkSplash">
	<h1 class="safetyNetworkHeadings">Settings</h1>

    <@s.form action="privacySettingsSave" cssClass="fullForm" theme="fieldid" >

        <div class="settingsHeader"><@s.text name="label.searchable" /></div>

        <div class="check">
            <@s.checkbox id="chkSearchable" name="searchableOnSafetyNetwork" />
            <label for="chkSearchable" class="label"><@s.text name="label.searchable_info" /></label>
        </div>

        <div class="settingsHeader"><@s.text name="label.autopublish" /></div>

        <div class="check">
            <@s.checkbox id="chkAutoPublish" name="autoPublish" />
            <label for="chkAutoPublish" class="label"><@s.text name="label.autopublish_info" /></label>
        </div>

        <div class="settingsHeader"><@s.text name="label.autoaccept" /></div>

        <div class="check">
            <@s.checkbox id="chkAutoAcceptConnections" name="autoAcceptConnections" />
            <label for="chkAutoAcceptConnections" class="label"><@s.text name="label.autoaccept_info" /></label>
        </div>

        <div class="actions">
            <@s.submit key="hbutton.save" /> <@s.text name="label.or"/> <a href="<@s.url action="safetyNetwork"/>"><@s.text name="label.cancel"/></a>
        </div>

    </@s.form>

</div>
