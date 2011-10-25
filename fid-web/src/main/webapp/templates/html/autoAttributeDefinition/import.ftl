${action.setPageType('auto_attribute', 'import_export')!}
<head>
	<@n4.includeStyle type="page" href="import"/>
	<@n4.includeScript src="importExport"/>
	
</head>

<@s.url id="updateUrl" namespace="/ajax" action="autoAttributeImportStatus" />

<#if validationFailed>
	<div class="validationErrors">
		<h2><@s.text name="label.validation_problems"/></h2>
		<p><@s.text name="message.validation_problems"/></p>
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
	</div>
	<div class="errorActions">
		<input type="button" onClick="location.href='<@s.url action="autoAttributeImportExport"/>'" value="<@s.text name='button.re_upload_import_file' />" />
		<span id="or"><@s.text name="label.or" /></span>
		<a href="/fieldid/w/dashboard"><@s.text name="label.cancel" /></a>
	</div>
</#if>

<#if task??>
<div class="importProgress">
	<div id="importStatus"></div>

	<script type="text/javascript">
		updateUrl = '${updateUrl}';
		getResponse(updateUrl, "get");
	</script>
</div>
</#if>