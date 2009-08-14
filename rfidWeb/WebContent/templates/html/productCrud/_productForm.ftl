<head>
	<script type="text/javascript" src="<@s.url value="/javascript/commentTemplates.js" />" ></script>
	<script type="text/javascript" src="<@s.url value="/javascript/product.js" />" ></script>
	<script type='text/javascript' src='<@s.url value="/javascript/updateAttributes.js"/>'></script>
	<script type='text/javascript' src='<@s.url value="/javascript/productRfidHandler.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateSerialNumber.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/customerUpdate.js"  />"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/changeJobSite.js" />"></script>
	<@n4.includeStyle href="pageStyles/product"/>
	<script type="text/javascript">
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		serialNumberUrl = '<@s.url action="generateSerialNumber" namespace="/ajax"  />';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"  />';
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"  />'; 
		customerChangeUrl = "<@s.url action="divisionList" namespace="/ajax" />";
		jobSiteChangeUrl = '<@s.url action="jobSite" namespace="/ajax" />';
	    productTypeChangeWarning = '<@s.text name="warning.producttypechange"/>';
	    originalProductType = ${productTypeId!0};
	    updateProductTypeUrl = '<@s.url action="productTypeChange" namespace="/ajax" />';
	    checkSerialNumberUrl = '<@s.url action="checkSerialNumber" namespace="/ajax" />';
	</script>
	<#include "/templates/html/common/_calendar.ftl"/>	
</head>

<#include "../common/_formErrors.ftl"/>
<@s.hidden name="uniqueID" id="uniqueID"/>

<#include "_productSerialAndRfidForm.ftl"/>

<#include "_productConfigForm.ftl"/>

