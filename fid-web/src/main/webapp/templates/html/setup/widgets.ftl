${action.setPageType('setup','widgets')!}

<title><@s.text name="label.setup" /></title>
<head> 
	<@n4.includeStyle href="settings" type="page"/>
</head>

<div class="setupList">
	<div class="setupOption signIn">
		<h1><@s.text name="label.login_url"/></h1>
		<p><@s.text name="label.login_url_msg"/></p>
		<div class="widget">
			<p>${loginUrl?html}</p>
		</div>
	</div>
	 
	<div class="setupOption embeddedCode">
		<h1><@s.text name="label.embedded_login_snipit"/></h1>
		<p><@s.text name="label.embedded_login_snipit_msg"/></p>
		<div class="widget">
			<p>
				<#assign snipit><iframe src="${embeddedLoginUrl}" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="300" ></iframe></#assign>
				${snipit?html}
			</p> 
		</div>
	</div>
</div>