<title><@s.text name="title.sign_in"/></title>

<head>
    <@n4.includeStyle href="pageStyles/login" />
    <@n4.includeScript src="jquery/watermark/jquery.watermark.js"/>
</head>

<div id="mainContent" class="login-page">
	<div class="titleBlock">
		${(securityGuard.primaryOrg.displayName?html)!} <span class="action"><@s.text name="title.sign_in"/></span>
    </div>

	<@s.form action="logIntoSystem" theme="fieldid" cssClass="minForm login-form" id="signInForm">
		<#include "/templates/html/common/_formErrors.ftl" />
		
        <@s.hidden name="signIn.normalLogin" id="normalLogin"/>
        <div id="normal_container" class="login-input">
            <img src="<@s.url value="/images/user-icon.png"/>"/><@s.textfield name="signIn.userName" id="userName"/>
            <img src="<@s.url value="/images/password-icon.png"/>"/><@s.password name="signIn.password" id="password"></@s.password>
        </div>

        <div class="oneLine remember-me">
            <span class="fieldHolder"><@s.checkbox name="signIn.rememberMe" theme="fieldidSimple" /><@s.text name="label.rememberme"/></span>
        </div>

        <div id="normalActions_container">
        <@s.submit cssClass="blue-submit" key="label.sign_in" id="signInButton"/>
            <a href="<@s.url action="forgotPassword"/>"><@s.text name="link.emailpassword"/></a>
            <a class="test" href="<@s.url value="/w/userAccountSearch"/>"><@s.text name="link.useremail"/></a>
        </div>

        <div class="links">
            <#if userLimitService.readOnlyUsersEnabled>
                <a href="<@s.url action="registerUser"/>"><span><@s.text name="label.requestanaccount"/></span></a>
            </#if>
            <#if securityGuard.plansAndPricingAvailable>
                <a href="${externalPlansAndPricingUrl}"><span><@s.text name="label.plans_and_pricing"/></span></a>
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






















































