<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
<title><@s.text name="app.title" /> - ${title!}</title>
<@n4.includeStyle href="reset" /> 
<@n4.includeStyle href="fieldid"/>
<!--[if IE 6]>
<@n4.includeStyle href="fieldid-ie6" />
<![endif]-->
<!--[if IE 7]>
<@n4.includeStyle href="fieldid-ie7" />
<![endif]-->
<@n4.includeStyle href="site_wide"/>
<@n4.includeStyle href="branding/default"/>


<link rel="shortcut icon" href="<@s.url value="/images/favicon.ico" />" type="image/x-icon" />		

<@n4.includeScript src="prototype"/>
<@n4.includeScript src="scriptaculous" />
<@n4.includeScript src="json2" />
<@n4.includeScript src="common" />

${head!} 