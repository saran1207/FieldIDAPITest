<title><@s.text name="title.password_reset"/></title>

<div id="mainContent">
	<div class="titleBlock">
		<h1><@s.text name="title.password_reset"/></h1>
	</div>
	
	<div class="minForm">
		<p class="actionInstructions">
			<@s.text name="message.passwordsent"/>
		</p>
		<div class="actions">
			<input type="submit" value="<@s.text name="label.return_to_sign_in"/>" onclick="return redirect( '<@s.url action="login"/>' );" />
		</div>
	</div>
</div>

<div id="secondaryContent">
	<#include "../public/_requestAccount.ftl"/>
</div>