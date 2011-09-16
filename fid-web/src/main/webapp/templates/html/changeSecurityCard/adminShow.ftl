${action.setPageType('user', 'change_mobile_passcode')!}

<head> 
	<@n4.includeStyle href="mobilePasscode" type="page"/>
</head>

<div class="passcodeActions">
	<input type="button" onClick="location.href='<@s.url action="adminRemoveMobilePasscode" uniqueID="${user.id}"/>'" value="<@s.text name='label.disable_mobile_passcode' />" />
	<input type="button" onClick="location.href='<@s.url action="adminEditMobilePasscode" uniqueID="${user.id}"/>'" value="<@s.text name='label.change_mobile_passcode' />" />
</div>
