<title><@s.text name="label.import_events"/></title>
<head>
	<@n4.includeScript src="eventImport"/>
	<@n4.includeStyle type="page" href="import"/>
</head>

<@s.url id="exportExample" action="downloadExampleEventExport" namespace="/file" includeRecommendationsAndDeficiencies="true"/>
<@s.url id="exportExampleMinimal" action="downloadExampleEventExport" namespace="/file" includeRecommendationsAndDeficiencies="false"/>

<div class="leftBox">
	<div class="infoSet">
		<h2>1. <@s.text name="label.select_import_type"><@s.param><@s.text name="label.event_type"/></@s.param></@s.text></h2>
		<p>
			<@s.text name="message.select_import_type"><@s.param><@s.text name="label.event_type"/></@s.param></@s.text>
			<@s.select theme="fieldid" id="eventTypeSelect" list="eventTypes" name="uniqueID" listValue="name" listKey="id" onchange="updateUploadForm();" />
		</p>
	</div> 

	<div class="infoSet">
		<h2>2. <@s.text name="label.download_template"/></h2>
		<p>
			<@s.text name="message.download_template"><@s.param><@s.text name="label.event_type"/></@s.param></@s.text>
		</p>
		<div id="templateLink">
			<input id="templateUrl" type="hidden" value="${exportExample}" />
			<input id="minimalTemplateUrl" type="hidden" value="${exportExampleMinimal}" />
			<p>
				<a id="downloadTemplate"><@s.text name="label_download_excel"/>&nbsp;<@s.text name="label.with_observations"/></a>
				<br/>
				<a id="downloadMinimalTemplate"><@s.text name="label_download_excel"/>&nbsp;<@s.text name="label.without_observations"/></a>
			</p>
		</div>
	</div>

	<div class="infoSet">
		<h2>3. <@s.text name="label.upload"/></h2>
		<p><@s.text name="message.upload"><@s.param><@s.text name="label.event_type"/></@s.param></@s.text></p>
		
		<@s.form id="uploadForm" action="importEvents" cssClass="fullForm fluentSets" theme="fieldid" method="POST" enctype="multipart/form-data">
			<@s.hidden id="uploadEventTypeId" name="uniqueID" value=""/>
			<@s.file id="importDoc" name="importDoc" size="30"/>
			
			<@s.submit id="submitImport" key="label.start_import"/>
			<span id="or"><@s.text name="label.or" /></span>
			<a href="<@s.url action="startEvent"/>"><@s.text name="label.cancel" /></a>
		</@s.form>
		
	</div>
</div>

<div class="rightBox">
	<div class="formatDate">
		<h4><@s.text name="label.formatting_dates" /></h4>
		<span><@s.text name="message.formatting_dates"><@s.param><@s.text name="label.event_type_date_fields"/></@s.param></@s.text></span>
	</div>
	<div class="faq">
		<div class="faqSection">
			<h4><@s.text name="label.import_export_question.6" /></h4>
			<span><@s.text name="label.import_export_answer.6" /></span>
		</div>
		<div class="faqSection">
			<h4><@s.text name="label.import_export_question.7" /></h4>
			<span><@s.text name="label.import_export_answer.7" /></span>
		</div>
		<div class="faqSection">
			<h4><@s.text name="label.import_export_question.2" /></h4>
			<span><@s.text name="label.import_export_answer.2" /></span>
		</div>
	</div>
</div>