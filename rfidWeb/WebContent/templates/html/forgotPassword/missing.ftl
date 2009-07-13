<title><@s.text name="title.resetpassword"/></title>
<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/public.css"/>"/>
</head>
<div class="easyForm">
	<p class="instructions">
		<@s.text name="error.resetlinkinvalid"/>
	</p>
	<div class="formAction">
		<input type="submit" value="<@s.text name="label.login"/>" onclick="return redirect( '<@s.url action="login"/>' );" /> 
		<input type="submit" value="<@s.text name="label.resetpassword"/>" onclick="return redirect( '<@s.url action="forgotPassword"/>' );" />
	</div>
	
	
</div>
