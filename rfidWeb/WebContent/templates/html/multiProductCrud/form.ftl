<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>
<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/steps.css"/>" />
	<script type="text/javascript" src="<@s.url value="/javascript/steps.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/commentTemplates.js" />" ></script>
	<script type="text/javascript" src="<@s.url value="/javascript/product.js" />" ></script>
	<script type='text/javascript' src='<@s.url value="/javascript/updateAttributes.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/customerUpdate.js"  />"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/changeJobSite.js" />"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/multiAddProduct.js" />"></script>
	<link rel="styleSheet" type="text/css" href="<@s.url value="/style/pageStyles/product.css" />">
	<link rel="styleSheet" type="text/css" href="<@s.url value="/style/pageStyles/multiadd.css" />">
	<script type="text/javascript">
		var loading_holder = '${loaderDiv?js_string}';

		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"  />'; 
		customerChangeUrl = "<@s.url action="divisionList" namespace="/ajax" />";
		jobSiteChangeUrl = '<@s.url action="jobSite" namespace="/ajax" />';
	    productTypeChangeWarning = '<@s.text name="warning.producttypechange"/>';
	    originalProductType = ${productTypeId!0};
	    updateProductTypeUrl = '<@s.url action="productTypeChange" namespace="/ajax" />';
	    
	    function moveToStep4() {
	    	
	    }
	</script>
	<#include "/templates/html/common/_calendar.ftl"/>
</head>

${action.setPageType('product', 'multi_add')!}

<#if limits.assetsMaxed>
	<div class="limitWarning">
	<@s.text name="label.exceeded_your_asset_limit">
		<@s.param>${limits.assetsMax}</@s.param>
	</@s.text>
	</div>
<#else>
	<div id="steps">
		<div class="step">
			<@s.form id="step1form" cssClass="inputForm" theme="css_xhtml">
				<#include "_step1.ftl" />
			</@s.form>
		</div>
		
		<@s.form action="generateSerials" id="step23Form" namespace="/ajax" theme="fieldidSimple">
			<div class="step stepClosed">
				<#include "_step2.ftl" />
			</div>
			
			<div class="step stepClosed">
				<#include "_step3.ftl" />
			</div>
		</@s.form>
		
		<div class="step stepClosed">
			
			<#include "_step4.ftl" />
			
		</div>
		
		<div id="cancelButton" class="stepAction" >
			<@s.url id="cancelUrl" action="home" />
			<@s.submit key="label.cancel_multi_add" id="cancel" onclick="return redirect('${cancelUrl}');"/>
		</div>
		
	</div>
</#if>