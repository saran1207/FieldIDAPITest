<head>
	<@n4.includeStyle type="page" href="customers"/>
</head>
${action.setPageType('customer', 'import_export')!}
<h2 class="sectionTitle"><@s.text name="label.import_customers" /></h2>

<#include "../common/_formErrors.ftl"/>
<@s.form id="uploadForm" action="importCustomers" cssClass="fullForm fluentSets" theme="fieldid" method="POST" enctype="multipart/form-data">	
	<label class="label" for="importDoc"><@s.text name="label.import_file" />:</label>
	<@s.file id="importDoc" name="importDoc" size="30" />
	
	<@s.submit key="label.import" />
	<span id="or"><@s.text name="label.or" /></span>
	<a id="backToLink" href="<@s.url action="customerImportExport"/>"><@s.text name="label.cancel" /></a>
</@s.form>

<#if validationFailed>
	<h2 class="sectionTitle"><@s.text name="label.validation_problems" /></h2>
	<table class="list">
		<tr>
			<th><@s.text name="label.row" /></th>
			<th><@s.text name="label.problem" /></th>
		</tr>
		<#list failedValidationResults as result>
		<tr>
			<td>${result.row}</td>
			<td>${result.message}</td>
		</tr>
		</#list>
	</table>
</#if>