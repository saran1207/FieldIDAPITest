<head>
	<@n4.includeStyle type="page" href="importExport"/>
</head>

<h2 class="sectionTitle largeText"><@s.text name="label.import_export" /></h2>

<div id="leftBox">
	<p><@s.text name="label.import_export.desc" /></p>
	
	<div id="importBox">
		<h3><@s.text name="label.import" /></h3>
		<a href='${importUrl}'><@s.text name="label.upload_excel_or_csv" /></a>
		<a href='${exportExample}'><@s.text name="label.download_excel_template" /></a>
	</div>
	
	<#if exportExcel?exists||exportCsv?exists>
		<div id="exportBox">
			<h3><@s.text name="label.export" /></h3>
			<#if exportExcel?exists>
				<a href='${exportExcel}' class='lightview' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.excel_file" /></a>
			</#if>
			<#if exportCsv?exists>
				<a href='${exportCsv}' class='lightview' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.csv_file" /></a>
			</#if>
		</div>
	</#if>
</div>

<div id="rightBox">
	<h3><@s.text name="label.import_export_questions" /></h3>
	<div class="faq">
		<h4><@s.text name="label.import_export_question.1" /></h4>
		<span><@s.text name="label.import_export_answer.1" /></span>
	</div>
	<div class="faq">
		<h4><@s.text name="label.import_export_question.2" /></h4>
		<span><@s.text name="label.import_export_answer.2" /></span>
	</div>
	<div class="faq">
		<h4><@s.text name="label.import_export_question.3" /></h4>
		<span><@s.text name="label.import_export_answer.3" /></span>
	</div>
	<div class="faq">
		<h4><@s.text name="label.import_export_question.4" /></h4>
		<span><@s.text name="label.import_export_answer.4" /></span>
	</div>
	<div class="faq">
		<h4><@s.text name="label.import_export_question.5" /></h4>
		<span><@s.text name="label.import_export_answer.5" /></span>
	</div>
</div>