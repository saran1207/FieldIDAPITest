${action.setPageType('asset', 'import')!}
<head>
	<@n4.includeScript src="assetImport"/>
	<@n4.includeStyle type="page" href="assetImport"/>
</head>
<@s.url id="exportExample" action="downloadExampleAssetExport" namespace="/file"/>

<div class="leftBox">
	<div class="infoSet">
		<h2><@s.text name="label.select_asset_type"/></h2>
		<p>
			<@s.text name="message.select_asset_type"/>
			<@s.select theme="fieldid" id="assetTypeSelect" list="assetTypes" name="assetTypeId" listValue="name" listKey="id" onchange="updateUploadForm();" />
		</p>
	</div> 

	<div class="infoSet">
		<h2><@s.text name="label.download_template"/></h2>
		<p>
			<@s.text name="message.download_template"/>
		</p>
		<div id="templateLink">
			<input id="templateUrl" type="hidden" value="${exportExample}" />
			<p>
				<a id="downloadTemplate"><@s.text name="label_download_excel"/></a>
			</p>
		</div>
	</div>

	<div class="infoSet">
		<h2><@s.text name="label.upload"/></h2>
		<p><@s.text name="message.upload"/></p>
		
		<@s.form id="uploadForm" action="importAssets" cssClass="fullForm fluentSets" theme="fieldid" method="POST" enctype="multipart/form-data">
			<@s.hidden id="uploadAssetTypeId" name="assetTypeId" value=""/>
			<@s.file id="importDoc" name="importDoc" size="30"/>
			
			<@s.submit id="submitImport" key="label.start_import"/>
			<span id="or"><@s.text name="label.or" /></span>
			<a href="<@s.url action="home"/>"><@s.text name="label.cancel" /></a>
		</@s.form>
		
	</div>
</div>

<div class="rightBox">
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