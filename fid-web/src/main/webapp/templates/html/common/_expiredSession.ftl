<@n4.includeScript src="sessionTimeout" />
<script type="text/javascript">
	sessionTestUrl = "<@s.url action="testSession" namespace="/ajax"/>";
	loginUrl = "<@s.url action="login" namespace="/aHtml"/>";
	loginWindowTitle = '<@s.text name="title.sessionexpired"/>';
	loggedInUserName = '${(Session.sessionUser.userName)!}';
	tenantName = '${(Session.sessionUser.tenant.name)!}';
	sessionTimeOut = ${currentSessionTimeout!30};
</script>
