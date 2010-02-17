<head>
	<@n4.includeStyle type="page" href="customers"/>
</head>
${action.setPageType('customer', 'import_export')!}
<h2 class="sectionTitle largeText"><@s.text name="label.import_export" /></h2>

<@s.url id="exportExcel" action="customerExport" namespace="/aHtml" exportType="excel" />
<@s.url id="exportCsv" action="customerExport" namespace="/aHtml" exportType="csv" />

<@s.url id="importUrl" action="showImportCustomers"/>

<div id="leftBox">
	<p><@s.text name="label.import_export.desc" /></p>
	
	<div id="importBox">
		<h3><@s.text name="label.import_customers" /></h3>
		<a href='${importUrl}'><@s.text name="label.upload_excel_or_csv" /></a>
		<a href=''><@s.text name="label.download_customer_excel_template" /></a>
	</div>
	
	<div id="exportBox">
		<h3><@s.text name="label.export_customers" /></h3>
		<a href='${exportExcel}' class='lightview' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.excel_file" /></a>
		<a href='${exportCsv}' class='lightview' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.csv_file" /></a>
	</div>
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
