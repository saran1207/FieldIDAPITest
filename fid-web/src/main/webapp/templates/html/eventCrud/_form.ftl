	<head>
		<script type="text/javascript" src="<@s.url value="/javascript/commentTemplates.js" />" ></script>
		<script type="text/javascript" src="<@s.url value="/javascript/event.js" />" ></script>
		<script type="text/javascript" src="<@s.url value="/javascript/eventBook.js"/>" ></script>
		<script type="text/javascript">
			changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="ajax"   />';
			updateEventBooksUrl = '<@s.url action="eventBooks" namespace="ajax"   />';
			assetTypeId = ${asset.type.id}
			var proofTestTypes = ${json.toJson( proofTestTypesUpload )}
		</script>
		<script type="text/javascript">
			function imageFileUploaded( fileName, directory ){
				$("singleFileUpload").remove();
				$("proofTestUploadFile").show();
				$("newFile").value = "true";
				$("proofTestDirectory").value = directory;
			}
			
			function uploadAnotherFile() { 
				var iframe = '<iframe id="singleFileUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />"  scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="20" ></iframe>';

				$( "proofTestUploadFile" ).hide();
				$( "proofTestUpload" ).insert( { top: iframe } );
				$("newFile").value = "true";
				$("proofTestDirectory").value = "";
			}
			
		</script>
		
		<@n4.includeStyle type="page" href="event" />
		
		
		<#include "/templates/html/common/_calendar.ftl"/>
	</head>
	<title>${(eventType.name)!} <@s.text name="label.on"/> ${asset.serialNumber}</title>
	
	<#include "/templates/html/common/_formErrors.ftl" />
	
	<#include "_eventSummaryForm.ftl"/>

	<#include "_attributes.ftl"/>

	<#if event.id?exists && action.isParentAsset() >
		<div class="infoSet">
			<label class="label"><@s.text name="label.result"/></label>
			<span class="fieldHolder">
				<@s.select name="result" list="results" listKey="name()" listValue="%{getText( label )}" />
			</span>
		</div>
	</#if>
	
	<#assign formEvent=event>
	<#assign identifier="eventForm">
	<#include "_event.ftl" />
	
	<#if action.isParentAsset() && !eventType.supportedProofTests.isEmpty() >
		<#include "_proofTest.ftl"/>
	</#if>
	
	<#include "_postEventForm.ftl" />
		
	<div class="attachments">
		<#include "../common/_attachedFilesForm.ftl" />
	</div>
