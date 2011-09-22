${action.setPageType('my_account', 'mobile_passcode')!}

<head> 
	<@n4.includeStyle href="mobilePasscode" type="page"/>
</head>

<#if !mobilePasscodeSet>
	<div class="initialMessage" >
		<div class="textContainer">
			<h1><@s.text name="label.setup_mobile_passcode" /></h1>
			<p><@s.text name="message.setup_mobile_passcode" /></p>
			<div class="eventActions">
				<input type="button" onClick="location.href='<@s.url action="editMobilePasscode"/>'" value="<@s.text name='label.create_mobile_passcode' />" />
			</div>
		</div>
	</div>
	
<#else>
<div class="passcodeActions">
<input type="button" onClick="location.href='<@s.url action="removeMobilePasscode"/>'" value="<@s.text name='label.disable_mobile_passcode' />" />
</div>
<div class="passcodeActions">
<input type="button" onClick="location.href='<@s.url action="editMobilePasscode"/>'" value="<@s.text name='label.change_mobile_passcode' />" />
</div>
</#if>