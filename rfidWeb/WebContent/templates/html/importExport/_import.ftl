<head>
	<@n4.includeStyle type="page" href="importExport"/>
	<script language="javascript" src="javascript/importExport.js"> </script>
</head>
<h2 class="sectionTitle"><@s.text name="label.import" /></h2>

<#include "../common/_formErrors.ftl"/>
<@s.form id="uploadForm" action="${importAction}" cssClass="fullForm fluentSets" theme="fieldid" method="POST" enctype="multipart/form-data">
	<@hiddenFields />
	
	<label class="label" for="importDoc"><@s.text name="label.import_file" />:</label>
	<@s.file id="importDoc" name="importDoc" size="30" />
	
	<@s.submit key="hbutton.import" />
	<span id="or"><@s.text name="label.or" /></span>
	<a id="backToLink" href="${backToUrl}"><@s.text name="label.cancel" /></a>
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

<#if task??>
	<div id="importStatus"></div>

	<script type="text/javascript">
		updateUrl = '${updateUrl}';
		getResponse(updateUrl, "get");
	</script>
</#if>