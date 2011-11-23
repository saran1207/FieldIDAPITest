
<#include '../vendor/_vendorinfo.ftl'/>

<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>
<head>
	<@n4.includeStyle href="steps" />
    <title><@s.text name="title.safety_network"/></title>
	<script type="text/javascript" src="<@s.url value="/javascript/steps.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/commentTemplates.js" />" ></script>
	<script type="text/javascript" src="<@s.url value="/javascript/asset.js" />" ></script>
    <script type="text/javascript" src="<@s.url value="/javascript/autoschedule.js" />" ></script>
	<script type="text/javascript" src="<@s.url value="/javascript/event.js" />" ></script>
	<script type='text/javascript' src='<@s.url value="/javascript/updateAttributes.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/multiAddAsset.js" />"></script>
	<@n4.includeStyle type="page" href="asset"/>
	<@n4.includeStyle type="page" href="multiadd"/>
	<@n4.includeStyle type="page" href="../newCss/event/event_schedule"/>
    <@n4.includeStyle href="vendor" type="page"/>
    <style>
        #steps {
            padding-left:15px;
            width: 700px;
        }
    </style>
	<script type="text/javascript">
		var loading_holder = '${loaderDiv?js_string}';

		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"  />';
	    assetTypeChangeWarning = '<@s.text name="warning.assettypechange"/>';
	    originalAssetType = ${assetTypeId!0};
	    updateAssetTypeUrl = '<@s.url action="bulkRegAssetTypeChange" namespace="/ajax" />';
	    updateAutoScheduleUrl = '<@s.url action="updateAutoSchedule" namespace="/ajax"/>';

	    function moveToStep4() {

	    }
	</script>
</head>

<#assign bulkRegister = true>

<div id="steps">
	<div class="step">
		<@s.form id="step1form" theme="fieldid" cssClass="fullForm fluidSets">
			<#include "_step1.ftl" />
		</@s.form>
	</div>

	<div class="step stepClosed">
		<#include "_step2.ftl" />
	</div>

	<div id="cancelButton" class="stepAction" >
		<@s.submit key="label.cancel_bulk_register" id="cancel" onclick="return redirect('/fieldid/w/dashboard');"/>
	</div>

</div>