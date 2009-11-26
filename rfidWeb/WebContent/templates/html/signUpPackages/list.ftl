<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>
<title><@s.text name="title.sign_up_for_an_account"/></title>

<div id="mainContent" class="fullScreen">

	<h1><@s.text name="title.sign_up_for_an_account"/></h1>
	<#assign purchaseAction="signUpAdd"/>
	<#assign purchaseLabel="label.sign_up_now"/>
	<#include "_packagesListing.ftl"/>
	
	<div>
		<@s.text name="label.have_an_account_already"/> <a href="<@s.url action="login" namespace="/"/>"><@s.text name="label.return_to_sign_in"/></a>
	</div>
</div>