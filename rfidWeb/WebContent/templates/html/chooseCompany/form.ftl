<title><@s.text name="title.chooseacompany"/></title>

<div id="mainContent">
	
	<div class="titleBlock">	
		<h1><@s.text name="title.chooseacompany"/></h1>
	</div>
	
	
	<@s.form action="signInToCompany" theme="fieldid" cssClass="minForm" >
		<#include "../common/_formErrors.ftl"/>
		<p class="actionInstructions"><@s.text name="instructions.choose_company"/></p>
		
		<label class='label'><@s.text name="label.companyid"/></label>
		<@s.textfield name="companyID" value="" id="companyId"/>
		<div class="actions">
			<@s.submit key="label.find_sign_in"/>
		</div>
	</@s.form>
	
</div>
<div id="secondaryContent">
	<#include "../public/_plansAndPrices.ftl"/>
</div>

<script type="text/javascript">
	$('companyId').focus();
</script> 