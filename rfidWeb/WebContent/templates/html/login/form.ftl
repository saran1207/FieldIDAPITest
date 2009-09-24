<title>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/></title>
<head>
	<style type="text/css">
		#signInForm {
			margin: 10px 0px;
		}
	</style>
</head>
<div id="mainContent">
	<div class="titleBlock">
		<h1>${(securityGuard.primaryOrg.displayName?html)!} <@s.text name="title.sign_in"/></h1>
		<p class="titleSummary"><@s.text name="instruction.already_have_a_field_id_account"/></p>
	</div>
	
	<@s.form action="logIntoSystem" theme="fieldid" cssClass="minForm" id="signInForm">
		<#include "/templates/html/common/_formErrors.ftl" />
		<@s.hidden name="normalLogin" id="normalLogin"/>
		<div id="normal_container" class="togglable">
			<label class="label"><@s.text name="label.username"/></label>
			<@s.textfield name="userName" id="userName"/>
			
			<label class="label"><@s.text name="label.password"/></label>
			<@s.password name="password" id="password"/>
		</div>
		
		<div id="secureRfid_container" class="togglable" style="display:none">
			<label class="label"><@s.text name="label.securityrfidnumber"/></label>
			<@s.password name="secureRfid" id="secureRfidNumber"/>
		</div>
		<div class="oneLine">
			<span class="fieldHolder"><@s.checkbox name="rememberMe" theme="fieldidSimple" /><@s.text name="label.rememberme"/></span>
		</div>
		
		
		<div class="actions togglable" id="normalActions_container" style="display:none" > 
			<@s.submit key="label.sign_in" id="signInButton"/> <@s.text name="label.or"/> <a href="#" onclick="return showSecurityCardSignIn();"><@s.text name="label.sign_in_with_security_card"/></a>
		</div>
		
		<div class="actions togglable" id="secureRfidActions_container" > 
			<@s.submit key="label.sign_in" id="signInWithSecurityButton"/> <@s.text name="label.or"/> <a href="#" onclick="return showNormalSignIn();"><@s.text name="label.sign_in_with_user_name"/></a>
		</div>
				
	</@s.form>
	
	<ul id="otherActions">
  		<li><label class="label"><@s.text name="label.help"/>:</label> <span><a href="<@s.url action="forgotPassword"/>"><@s.text name="link.emailpassword"/></a></span></li>
  		<li><label class="label">${securityGuard.tenantName?html}</label> <span><a href="<@s.url action="chooseCompany"/>"><@s.text name="label.is_not_the_company_i_want"/></a></span></li>
	</ul>

</div>
<div id="secondaryContent">
	<#include "../public/_plansAndPricing.ftl"/>
	<#include "../public/_requestAccount.ftl"/>
</div>

<@n4.includeScript>
	function showSecurityCardSignIn() {
		hideAllTogglable();
		
		$('normalLogin').value ='false';
		$('secureRfid_container').show();
		$('secureRfidActions_container').show();	
		$('secureRfidNumber').focus();
		return false;
	}
	
	function showNormalSignIn() {
		hideAllTogglable();
		
		$('normalLogin').value ='true';
		$('normal_container').show();
		$('normalActions_container').show();	
		$('userName').focus();
		return false;
	}
	
	function hideAllTogglable() {
		$$(".togglable").each( function(element) {
				element.hide();
			});
	}
	
	<#if normalLogin > 
		showNormalSignIn();
	<#else>
		showSecurityCardSignIn();
	</#if>
</@n4.includeScript>