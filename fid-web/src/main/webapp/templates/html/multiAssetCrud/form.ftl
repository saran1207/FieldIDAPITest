<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>
<head>
	<@n4.includeStyle href="steps" />
	<script type="text/javascript" src="<@s.url value="/javascript/steps.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/commentTemplates.js" />" ></script>
	<script type="text/javascript" src="<@s.url value="/javascript/asset.js" />" ></script>
    <script type="text/javascript" src="<@s.url value="/javascript/autoschedule.js" />" ></script>
	<script type="text/javascript" src="<@s.url value="/javascript/event.js" />" ></script>
	<script type='text/javascript' src='<@s.url value="/javascript/updateAttributes.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/multiAddAsset.js" />"></script>
	<@n4.includeStyle type="page" href="asset"/>
	<@n4.includeStyle type="page" href="multiadd"/>
	<script type="text/javascript">
		var loading_holder = '${loaderDiv?js_string}';

		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"  />'; 
	    assetTypeChangeWarning = '<@s.text name="warning.assettypechange"/>';
	    originalAssetType = ${assetTypeId!0};
	    updateAssetTypeUrl = '<@s.url action="assetTypeChange" namespace="/ajax" />';
	    updateAutoScheduleUrl = '<@s.url action="updateAutoSchedule" namespace="/ajax"/>';
	    
	    function moveToStep4() {
	    	
	    }
	</script>
</head>
<#if lineItem?exists>
	${action.setPageType('newasset', 'add_with_order')!}
<#else>
	${action.setPageType('newasset', 'multi_add')!}
</#if>

<div id="steps">
	<div class="step">
		<@s.form id="step1form" theme="fieldid" cssClass="fullForm fluidSets">
			<#include "_step1.ftl" />
		</@s.form>
	</div>
	
	<@s.form action="generateIdentifiers" id="step23Form" namespace="/ajax" theme="fieldidSimple">
        <input type="hidden" name="assetTypeId" id="step23AssetTypeId" value=""/>

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