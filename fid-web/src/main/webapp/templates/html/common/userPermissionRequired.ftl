<title><@s.text name="title.you_do_not_have_access_to_this_action"/></title>
<div id="noPermissionMessage">
	<p>You do not have the permissions to perform this action.</p>
	<#if sessionUser.employeeUser>
		<p>Please contact your Field ID administrator for more information.</p>
	</#if>
	
	<p><@s.text name="label.return_to"/> <a href="/fieldid/w/dashboard" ><@s.text name="label.home"/></a></p>
</div>		
