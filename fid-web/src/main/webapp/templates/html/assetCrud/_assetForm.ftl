<head>
	<@n4.includeScript src="commentTemplates.js" />
	<@n4.includeScript src="asset.js" />
	<@n4.includeScript src="updateAttributes.js"/>
	<@n4.includeScript src="assetRfidHandler.js"/>
	<@n4.includeScript src="generateSerialNumber.js"/>
	<@n4.includeScript src="lockSubmitButtons.js"/>
	<@n4.includeStyle type="page" href="asset"/>
	<@n4.includeScript src="safetyNetworkSmartSearch.js" />
	<@n4.includeStyle href="safetyNetworkSmartSearch.css" type="feature"/>
	<script type="text/javascript">
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"/>';
		serialNumberUrl = '<@s.url action="generateSerialNumber" namespace="/aHtml"/>';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"/>';
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"/>'; 
	    assetTypeChangeWarning = '<@s.text name="warning.assettypechange"/>';
	    originalAssetType = ${assetTypeId!0};
	    updateAssetTypeUrl = '<@s.url action="assetTypeChange" namespace="/ajax"/>';
	    checkSerialNumberUrl = '<@s.url action="checkSerialNumber" namespace="/ajax"/>';
	</script>
	<#include "/templates/html/common/_calendar.ftl"/>	
</head>

<#include "../common/_formErrors.ftl"/>
<@s.hidden name="uniqueID" id="uniqueID"/>

<#include "_assetTypeForm.ftl"/>
<#include "_assetSerialAndRfidForm.ftl"/>
<#include "_assetConfigForm.ftl"/>

