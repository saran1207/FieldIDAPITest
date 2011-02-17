<head>
	<title><@s.text name="title.chooseacompany"/></title>
	<style>
		#pageContent{
			border-style: none;
			background: transparent !important;
		}
	</style>
</head>

<div id="plansAndPricing">
	<#include "_plansAndPricing.ftl"/>
</div>

<div id="searchById">
	<div class="titleBlock">	
		<h1><@s.text name="title.existing_customers"/></h1>
	</div>
	
	
	<@s.form id="siteForm" action="signInToCompany" theme="fieldid" cssClass="minForm" >
		<#include "../common/_formErrors.ftl"/>
		<p class="actionInstructions"><@s.text name="instructions.choose_company"/></p>
		
		<label class='label'><@s.text name="label.site_address"/></label>
		<div class="prefix"><@s.text name="label.http"/></div><@s.textfield name="companyID" value="" id="companyId"/><div><@s.text name="label.fieldid_dot_com"/></div>
		<div class="actions">
			<@s.submit key="label.login"/>
		</div>
	</@s.form>
</div>

<div id="searchByEmail">
	<div class="titleBlock">	
		<h1><@s.text name="title.i_forgot_my_site"/></h1>
	</div>
	
	<@s.form action="findCompany" theme="fieldid" cssClass="minForm" >
		<#include "../common/_formErrors.ftl"/>
		<p class="actionInstructions"><@s.text name="instructions.find_site"/></p>
		
		<label class='label'><@s.text name="label.emailaddress"/></label>
		<@s.textfield name="email" value="" id="email"/>
		<div class="actions">
			<@s.submit key="label.find_my_site_address"/>
		</div>
	</@s.form>
</div>

<script type="text/javascript">
	$('companyId').focus();
</script> 