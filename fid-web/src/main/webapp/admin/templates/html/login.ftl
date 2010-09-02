<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Field ID Administration</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<@n4.includeStyle href="admin/style"/>
	<@n4.includeStyle href="fieldid"/>
</head>
<body style="background: #FFF url(../images/admin/back8.jpg) no-repeat fixed left top;" >
	<div id="header">
		<h1>Field ID Admin Console</h1>
	</div>
	
	<div id="container">
		<div id="content">
			<@s.form id="loginForm" action="signIntoSystem" theme="fieldid" method="post">
				<div class="infoSet">
					<div class="label">Username:</div>
					<@s.textfield name="username"/>
				</div>
				<div class="infoSet">
					<div class="label">Password:</div>
					<@s.password name="password"/>
				</div>
				<@s.submit />
			</@s.form>
		</div>
	</div>
	
</body>
</html>

