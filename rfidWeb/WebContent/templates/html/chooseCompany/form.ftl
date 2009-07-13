<title><@s.text name="title.chooseacompany"/></title>
<@s.form action="signInToCompany" theme="fieldid" cssClass="easyForm" >
	<#include "../common/_formErrors.ftl"/>
	<label class='label'><@s.text name="label.companyid"/></label>
	<@s.textfield name="companyID" value="" id="companyId"/>
	<div class="formAction">
		<@s.submit key="label.continue"/>
	</div>
</@s.form>

<script type="text/javascript">
	$('companyId').focus();
</script>