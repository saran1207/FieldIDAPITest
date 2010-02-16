<#include "/templates/html/common/_lightView.ftl" />
<@n4.includeScript src="sessionTimeout" />
<script type="text/javascript">
	sessionTestUrl = "<@s.url action="testSession" namespace="/ajax"/>";
	loginUrl = "<@s.url action="login" namespace="/ajax"/>";
	loginWindowTitle = '<@s.text name="title.sessionexpired"/>';
	loggedInUserName = '${(Session.sessionUser.userName)!}';
	tenantName = '${(Session.sessionUser.tenant.name)!}';
</script>
