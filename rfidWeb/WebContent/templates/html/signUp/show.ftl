<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>

<title><@s.text name="title.account_created"/></title>

<div id="mainContent">
	<h1><@s.text name="title.account_created"/></h1>
	<p>--- Congratulations you have signed up for Field ID.</p>  
	<a href="${action.getLoginUrlForTenant(signUp.tenantName)}"><span><@s.text name="label.sign_in_now"/></span></a>
</div>	  