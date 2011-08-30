<head>
	<@n4.includeStyle href="chosen/chosen.css"/>
	<@n4.includeScript src="jquery-1.4.2.min.js"/>
	<@n4.includeScript>jQuery.noConflict();</@n4.includeScript>	
	<@n4.includeScript src="commentTemplates.js" />
	<@n4.includeScript src="asset.js" />
	<@n4.includeScript src="event.js" />
    <@n4.includeScript src="autoschedule.js" />
	<@n4.includeScript src="updateAttributes.js"/>
	<@n4.includeScript src="assetRfidHandler.js"/>
	<@n4.includeScript src="generateIdentifier.js"/>
	<@n4.includeScript src="lockSubmitButtons.js"/>
	<@n4.includeStyle type="page" href="asset"/>
	<@n4.includeScript src="safetyNetworkSmartSearch.js" />
	<@n4.includeStyle href="safetyNetworkSmartSearch.css" type="feature"/>
	<@n4.includeStyle href="newCss/event/event_schedule"/>
	<script type="text/javascript">
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"/>';
		identifierUrl = '<@s.url action="generateIdentifier" namespace="/aHtml"/>';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"/>';
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"/>'; 
	    assetTypeChangeWarning = '<@s.text name="warning.assettypechange"/>';
	    originalAssetType = ${assetTypeId!0};
	    updateAssetTypeUrl = '<@s.url action="assetTypeChange" namespace="/ajax"/>';
	    updateAutoScheduleUrl = '<@s.url action="updateAutoSchedule" namespace="/ajax"/>';
	    checkIdentifierUrl = '<@s.url action="checkIdentifier" namespace="/ajax"/>';
	</script>
</head>



<#include "../common/_formErrors.ftl"/>
<@s.hidden name="uniqueID" id="uniqueID"/>

<#include "_assetTypeForm.ftl"/>
<div class="twoColumnLeft">
	<#include "_assetIdentifierAndRfidForm.ftl"/>
	<#include "_assetConfigForm.ftl"/>
</div>
<div class="twoColumnRight">
	<#include "_fileAttachment.ftl"/>
	<#if isAddForm>
		<#include "/templates/html/eventCrud/_schedules.ftl" />
	</#if>
</div>

<script src="javascript/chosen/chosen.jquery.js" type="text/javascript"></script>
<script type="text/javascript"> jQuery(".chzn-select").chosen(); </script>
