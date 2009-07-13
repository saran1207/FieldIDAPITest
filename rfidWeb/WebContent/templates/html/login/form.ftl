<title><@s.text name="title.login"/></title>
<head>
	<link rel="StyleSheet" href="/fieldid/style/tabNav.css" type="text/css"/>
	<script type="text/javascript" src="<@s.url value="/javascript/tabNav.js"/>"></script>
</head>
<@s.form action="logIntoSystem" theme="fieldid" cssClass="easyForm">
	<#include "/templates/html/common/_formErrors.ftl" />
	<@s.hidden name="normalLogin" id="loginType"/>
	<div class="oneLine">
		<label><@s.text name="label.logininto"/>: </label>
		<label>${(securityGuard.tenantName?html)!}</label>
		<@s.hidden name="companyID" id="companyID" value="${securityGuard.tenantName}"/>
	</div>
	<p ><a href="<@s.url action="chooseCompany"/>"><@s.text name="label.anothercompany"/></a></p>
	
	
	<ul id="tabnav" class="tabnav">
		<li id="normal" class="selectedTab"><a href="javascript: void(0);" onclick="toggleTab('tabnav', 'normal'); $('loginType').value ='true';"><@s.text name="label.normal"/></a></li>
		<li id="secureRfid"><a href="javascript: void(0);" onclick="toggleTab('tabnav', 'secureRfid'); $('loginType').value ='false';"><@s.text name="label.securerfid"/></a></li>
	</ul>
	<div id="normal_container">
		<label class="label"><@s.text name="label.username"/></label>
		<@s.textfield name="userName" id="userName"/>
		
		<label class="label"><@s.text name="label.password"/></label>
		<@s.password name="password" id="password"/>
	</div>
	
	<div id="secureRfid_container" style="display:none">
		<label class="label"><@s.text name="label.securityrfidnumber"/></label>
		<@s.password name="secureRfid" id="secureRfidNumber"/>
	</div>
	<div class="oneLine">
		<span class="fieldHolder"><@s.checkbox name="rememberMe" theme="fieldidSimple" /><@s.text name="label.rememberme"/></span>
	</div>
	
	<div class="formAction"> 
		<@s.submit key="hbutton.login" id="loginButton"/>
	</div>
	
	<p>
		<a href="<@s.url action="forgotPassword"/>"><@s.text name="link.emailpassword"/></a>
	</p>
	
	<#if securityGuard.partnerCenterEnabled>
		<p>
			<a href="<@s.url action="registerUser"/>"><@s.text name="label.requestanaccount"/></a>
		</p>
	</#if>

		
		
</@s.form>



<script type="text/javascript">
	<#if normalLogin > 
		$('userName').focus();
	<#else>
		$('secureRfidNumber').focus();
		toggleTab('tabnav', 'secureRfid');
	</#if> 
</script>