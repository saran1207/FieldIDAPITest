${action.setPageType('auto_attribute', 'import_export')!}

<head>
	<@n4.includeScript src="autoAttributeImport"/>
	<@n4.includeStyle type="page" href="import"/>
	<@n4.includeStyle type="page" href="downloads" />
</head>

<@s.url id="exportExample" action="downloadExampleAutoAttributeExport" namespace="/file"/>
<@s.url id="exportExcel" action="autoAttributeExport" namespace="/aHtml"/>


<div class="leftBox">
	<div class="infoSet">
		<h2>1. <@s.text name="label.select_import_type"><@s.param><@s.text name="label.autoattribute"/></@s.param></@s.text></h2>
		<p>
			<@s.text name="message.select_import_type"><@s.param><@s.text name="label.autoattribute"/></@s.param></@s.text>
			<@s.select theme="fieldid" id="assetTypeSelect" list="assetTypes" name="assetTypeId" listValue="name" listKey="autoAttributeCriteria.id" onchange="updateUploadForm()" />
		</p>
	</div> 

	<div class="infoSet">
		<h2>2. <@s.text name="label.export_download_template"/></h2>
		<p>
			<@s.text name="message.download_template"><@s.param><@s.text name="label.autoattribute"/></@s.param></@s.text>
		</p>
		<div id="templateLink">
			<input id="templateUrl" type="hidden" value="${exportExample}" />
			<input id="exportUrl" type="hidden" value="${exportExcel}" />
			<p id="assetTemplate">
				<a id="exportLink" class='lightview' rel='ajax' title=' :: :: scrolling:true, autosize: true'><@s.text name="label.export_existing"><@s.param><@s.text name="label.autoattribute"/></@s.param></@s.text></a>
				<@s.text name="label.or"/>
				<a id="downloadTemplate"><@s.text name="label_download_excel"/></a>
			</p>
		</div>
	</div>

	<div class="infoSet">
		<h2>3. <@s.text name="label.upload"/></h2>
		<p><@s.text name="message.upload"><@s.param><@s.text name="label.autoattribute"/></@s.param></@s.text></p>
		
		<@s.form id="uploadForm" action="importAutoAttributes" cssClass="fullForm fluentSets" theme="fieldid" method="POST" enctype="multipart/form-data">
			<@s.hidden id="uploadAutoAttributeId" name="criteriaId" value=""/>
			<@s.file id="importDoc" name="importDoc" size="30"/>
			
			<@s.submit id="submitImport" key="label.start_import"/>
			<span id="or"><@s.text name="label.or" /></span>
			<a href="/fieldid/w/dashboard"><@s.text name="label.cancel" /></a>
		</@s.form>
		
	</div>
</div>

<div class="rightBox">
	<div class="formatDate">
		<h4><@s.text name="label.formatting_dates" /></h4>
		<span><@s.text name="message.formatting_dates"><@s.param><@s.text name="label.asset_type_date_fields"/></@s.param></@s.text></span>
	</div>
	<div class="faq">
		<div class="faqSection">
			<h4><@s.text name="label.import_export_question.1" /></h4>
			<span><@s.text name="label.import_export_answer.1" /></span>
		</div>
		<div class="faqSection">
			<h4><@s.text name="label.import_export_question.2" /></h4>
			<span><@s.text name="label.import_export_answer.2" /></span>
		</div>
		<div class="faqSection">
			<h4><@s.text name="label.import_export_question.3" /></h4>
			<span><@s.text name="label.import_export_answer.3" /></span>
		</div>
	</div>
</div>