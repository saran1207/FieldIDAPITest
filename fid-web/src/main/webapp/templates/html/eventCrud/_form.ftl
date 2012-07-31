	<head>
		<script type="text/javascript" src="<@s.url value="/javascript/commentTemplates.js" />" ></script>
		<script type="text/javascript" src="<@s.url value="/javascript/event.js" />" ></script>
        <script type="text/javascript" src="<@s.url value="/javascript/autoschedule.js" />" ></script>
		<script type="text/javascript" src="<@s.url value="/javascript/eventBook.js"/>" ></script>
		<script type="text/javascript">
			changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="ajax"   />';
			updateEventBooksUrl = '<@s.url action="eventBooks" namespace="ajax"   />';
		    updateAutoScheduleUrl = '<@s.url action="eventOwnerChange" namespace="/ajax"/>';
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

		<@n4.includeStyle href="newCss/event/event_base" />
		<@n4.includeStyle href="newCss/event/event_schedule" />
	</head>
	<title>${(eventType.name)!} <@s.text name="label.on"/> ${asset.identifier}</title>
    <input type="hidden" name="assetType" id="assetType" value="${asset.type.id}">
	
	<#include "/templates/html/common/_formErrors.ftl" />
	
	<#include "_eventSummaryForm.ftl"/>

	<#include "_attributes.ftl"/>


	<#assign formEvent=event>
	<#assign identifier="eventForm">
	<#include "_event.ftl" />

    <#if event.id?exists && !action.isOpen() && action.isParentAsset() >
        <h2><@s.text name="label.result"/></h2>
        <div class="infoSet">
            <label class="label"><@s.text name="label.result"/></label>
            <span class="fieldHolder">
                <@s.select name="result" list="results" listKey="name()" listValue="%{getText( label )}" />
            </span>
        </div>

        <div class="infoSet">
            <label class="label"><@s.text name="label.event_status"/></label>
            <span class="fieldHolder">
                <@s.select name="eventStatus" headerKey="" headerValue="" list="eventStatuses" listKey="id" listValue="displayName" theme="simple"/>
            </span>
        </div>
    </#if>
    <#if (!event.id?exists || action.isOpen()) && action.isParentAsset()>
        <#include "_result.ftl" />
    </#if>
	
	<#if action.isParentAsset() && !eventType.supportedProofTests.isEmpty() && securityGuard.proofTestIntegrationEnabled >
		<#include "_proofTest.ftl"/>
	</#if>
	
	<#include "_postEventForm.ftl" />
		
	<div class="attachments">
		<#include "../common/_attachedFilesForm.ftl" />
	</div>