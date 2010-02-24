<#if action.session.sessionBooted>
	<div id="sessionKickNotice" class="systemNotice">
		<strong><@s.text name="label.note"/></strong>: 
		<@s.text name="warning.session_kick_out"><@s.param>${sessionUser.userID}</@s.param></@s.text>
		 
	</div>
</#if>