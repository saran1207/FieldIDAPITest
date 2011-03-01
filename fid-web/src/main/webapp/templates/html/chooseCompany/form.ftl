<head>
	<title><@s.text name="title.chooseacompany"/></title>
	<@n4.includeStyle href="chooseCompany" type="page"/>
</head>

<div id="plansAndPricing">
	<#include "_plansAndPricing.ftl"/>
</div>

<@s.form id="siteForm" action="signInToCompany" theme="fieldid" cssClass="minForm" >
	<div id="searchById">
		<div class="titleBlock">	
			<h1><@s.text name="title.existing_customers"/></h1>
		</div>
		<#include "../common/_formErrors.ftl"/>
		<p class="actionInstructions"><@s.text name="instructions.choose_company"/></p>
		
		<label class='label'><@s.text name="label.site_address"/></label>
		<span class='prefix'><@s.text name="label.http"/></span><@s.textfield name="companyID" value="" id="companyId"/><span class='suffix'><@s.text name="label.fieldid_dot_com"/></span>
		<div class="actions">
			<@s.submit key="label.login"/>
		</div>
	</div>
	</@s.form>
	<@s.form id="emailForm" action="findCompany" theme="fieldid" cssClass="minForm" >
	<div id="searchByEmail">
		<div class="titleBlock">	
			<h1><@s.text name="title.i_forgot_my_site"/></h1>
		</div>
		<p class="actionInstructions"><@s.text name="instructions.find_site"/></p>
		
		<label class='label'><@s.text name="label.emailaddress"/></label>
		<@s.textfield name="email" value="" id="email"/>
		<div class="actions">
			<@s.submit key="label.find_my_site_address"/>
		</div>
	</div>
</@s.form>

<script type="text/javascript">
	$('companyId').focus();
</script> 