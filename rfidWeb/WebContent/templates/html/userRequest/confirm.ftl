<title><@s.text name="title.register" /></title>

<div id="mainContent">
	<div class="titleBlock">
		<h1><@s.text name="title.register"/></h1>
	</div>
	<div class="fullForm">
		<p class="actionInstructions">
			<@s.text name="message.user_request_sent">
				<@s.param>${userId}</@s.param> 
				<@s.param>${emailAddress}</@s.param>
			</@s.text>
		</p>
		<div class="actions">
			<input type="submit" value="<@s.text name="label.return_to_sign_in"/>" onclick="return redirect( '<@s.url action="login"/>' );" />
		</div>
	</div>
</div>

<div id="secondaryContent">
	<h2><@s.text name="label.how_soon_can_i_login"/></h2>
	<p class="titleSummary"><@s.text name="label.how_soon_can_i_login.full"><@s.param>${securityGuard.primaryOrg.name?html}</@s.param></@s.text></p>
	<h2><@s.text name="label.will_i_get_notified_when_i_can_sign_in"/></h2>
	<p class="titleSummary"><@s.text name="label.will_i_get_notified_when_i_can_sign_in.full"/></p>
</div>	

