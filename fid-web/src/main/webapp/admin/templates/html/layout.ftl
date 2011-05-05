<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Field ID Administration</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<@n4.includeScript src="prototype"/>
	<@n4.includeScript src="scriptaculous"/>
	<@n4.includeScript src="common"/>
    <@n4.includeStyle href="reset"/>
    <@n4.includeStyle href="fieldid"/>
	<@n4.includeStyle href="admin/style"/>	
	${head!}
</head>
<body>
	<div id="header">
        <a href="<@s.url namespace="/admin" action="signOut"/>"><@s.text name="label.logout" /></a>
		 <a href="/fieldid/admin/organizations.action" style="border:0;">
             <img src="../images/admin/admin-logo.png"/>
         </a>

         <#include '_navigation.ftl'/>
	</div>

	<script type="text/javascript">
		var currentUrl = window.location.href;
		var navSelect = $('navSelect');
		for (var i = 0; i < navSelect.length; i++) {
			if (currentUrl.indexOf(navSelect.options[i].value) != -1) {
				navSelect.selectedIndex = i;
				break;
			}	
		}
	</script>
	
	<div id="container">
		<div id="content">
			${body}
		</div>
	</div>
	
</body>
</html>