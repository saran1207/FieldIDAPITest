<head>
	<@n4.includeScript src="commentTemplates.js" />
	<@n4.includeScript src="product.js" />
	<@n4.includeScript src="updateAttributes.js"/>
	<@n4.includeScript src="productRfidHandler.js"/>
	<@n4.includeScript src="generateSerialNumber.js"/>
	<@n4.includeScript src="lockSubmitButtons.js"/>
	<@n4.includeStyle type="page" href="product"/>
	<@n4.includeScript src="safetyNetworkSmartSearch.js" />
	<@n4.includeStyle href="safetyNetworkSmartSearch.css" type="feature"/>
	<script type="text/javascript">
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"/>';
		serialNumberUrl = '<@s.url action="generateSerialNumber" namespace="/ajax"/>';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"/>';
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"/>'; 
	    productTypeChangeWarning = '<@s.text name="warning.producttypechange"/>';
	    originalProductType = ${productTypeId!0};
	    updateProductTypeUrl = '<@s.url action="productTypeChange" namespace="/ajax"/>';
	    checkSerialNumberUrl = '<@s.url action="checkSerialNumber" namespace="/ajax"/>';
	</script>
	<#include "/templates/html/common/_calendar.ftl"/>	
</head>

<#include "../common/_formErrors.ftl"/>
<@s.hidden name="uniqueID" id="uniqueID"/>
<#include "_safetyNetworkRegistration.ftl"/>
<#include "_productSerialAndRfidForm.ftl"/>

<#include "_productConfigForm.ftl"/>

