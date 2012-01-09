<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>
<head>
	<@n4.includeStyle href="steps" />
	<@n4.includeStyle href="chosen/chosen.css"/>
	<@n4.includeScript src="steps.js"/>
	<@n4.includeScript src="commentTemplates.js" />
	<@n4.includeScript src="asset.js" />
    <@n4.includeScript src="autoschedule.js" />
	<@n4.includeScript src="event.js" />
	<@n4.includeScript src="updateAttributes.js"/>
	<@n4.includeScript src="multiAddAsset.js" />
	<@n4.includeScript src="assetRfidHandler.js"/>
	<@n4.includeScript src="lockSubmitButtons.js"/>
	<@n4.includeStyle type="page" href="asset"/>
	<@n4.includeStyle type="page" href="multiadd"/>
	<@n4.includeStyle href="newCss/event/event_schedule"/>	
	<script type="text/javascript">
		var loading_holder = '${loaderDiv?js_string}';

		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"  />'; 
	    assetTypeChangeWarning = '<@s.text name="warning.assettypechange"/>';
	    originalAssetType = ${assetTypeId!0};
	    updateAssetTypeUrl = '<@s.url action="assetTypeChange" namespace="/ajax" />';
	    updateAutoScheduleUrl = '<@s.url action="updateAutoSchedule" namespace="/ajax"/>';
	    checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"/>';
	    	    
	</script>
</head>
<#if lineItem?exists>
	${action.setPageType('newasset', 'add_with_order')!}
<#else>
	${action.setPageType('newasset', 'multi_add')!}
</#if>

<div id="steps">
	<#assign isMultiAdd=true>	

	<div class="step">
		<@s.form id="step1form" theme="fieldid" cssClass="fullForm fluidSets">
			<#include "_step1.ftl" />
		</@s.form>
		<script src="javascript/chosen/chosen.jquery.js" type="text/javascript"></script>
		<script type="text/javascript"> jQuery(document).ready( function() { jQuery(".chzn-select").chosen(); } );</script>
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
		<@s.submit key="label.cancel_multi_add" id="cancel" onclick="return redirect('/fieldid/w/dashboard');"/>
	</div>

</div>


