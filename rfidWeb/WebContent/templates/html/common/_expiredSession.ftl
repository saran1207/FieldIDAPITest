<#include "/templates/html/common/_lightView.ftl" />
<script type="text/javascript" src="<@s.url value="/javascript/sessionTimeout.js" />"></script>
<script type="text/javascript">
	sessionTestUrl = "<@s.url action="testSession" namespace="/ajax"/>";
	loginUrl = "<@s.url action="login" namespace="/ajax"/>";
	loginWindowTitle = '<@s.text name="title.sessionexpired"/>';
	loggedInUserName = '${(Session.sessionUser.userName)!}';
	tenantName = '${(Session.sessionUser.tenant.name)!}';
</script>
