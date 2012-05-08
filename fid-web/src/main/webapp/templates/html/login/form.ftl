<title>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/></title>
<head>
    <@n4.includeStyle href="pageStyles/login" />
    <@n4.includeScript src="jquery/watermark/jquery.watermark.js"/>
</head>
<div id="mainContent" class="login-page">
	<div class="titleBlock">
		${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/>
	</div>
	
	<@s.form action="logIntoSystem" theme="fieldid" cssClass="minForm login-form" id="signInForm">
		<#include "/templates/html/common/_formErrors.ftl" />
		<@s.hidden name="signIn.normalLogin" id="normalLogin"/>
        <div id="normal_container" class="login-input">
			<@s.textfield name="signIn.userName" id="userName"/>
			<@s.password name="signIn.password" id="password"></@s.password>
            <div class="login-help">
                <a href="<@s.url action="forgotPassword"/>"><@s.text name="link.emailpassword"/></a>
            </div>
        </div>

		<div class="oneLine remember-me">
			<span class="fieldHolder"><@s.checkbox name="signIn.rememberMe" theme="fieldidSimple" /><@s.text name="label.rememberme"/></span>
		</div>	

		<div class="actions" id="normalActions_container"> 
			<@s.submit cssClass="blue-submit" key="label.sign_in" id="signInButton"/>
            <#if userLimitService.readOnlyUsersEnabled>
                <div class="request"><@s.text name="label.or"/><a href="<@s.url action="registerUser"/>"><span><@s.text name="label.requestanaccount"/></span></a></div>
            </#if>
            <#if securityGuard.plansAndPricingAvailable>
                <div class="request"><@s.text name="label.or"/><a href="${externalPlansAndPricingUrl}"><span><@s.text name="label.plans_and_pricing"/></span></a></div>
            </#if>
		</div>
						
	</@s.form>

</div>

<script>
    jQuery('#password').watermark('password');
    jQuery('#userName').watermark('username');

    document.observe("dom:loaded", function() {
        $('userName').focus();
    });
</script>

