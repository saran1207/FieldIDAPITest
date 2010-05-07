${action.setPageType('inspection', 'add')!}

<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>

<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/steps.css"/>" />
	<script type="text/javascript" src="<@s.url value="/javascript/steps.js"/>"></script>
	
	<@n4.includeScript src="inspection" />
	<@n4.includeScript src="multi_inspect" />
	<@n4.includeStyle type="page" href="inspection" />
	<#include "/templates/html/common/_calendar.ftl"/>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	
	<@n4.includeScript src="commentTemplates"/>
		
	<@n4.includeScript>
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"/>';
		var loading_holder = '${loaderDiv?js_string}';
		
		onDocumentLoad(function(){ toStep(1); });
		
		var asset = null;
		<#list assets as asset>
			asset = new Object();
			asset.id = ${asset.id};
			asset.ownerId = ${asset.owner.id};
			asset.location = "${(asset.location?js_string)!}";
			asset.productStatusId = "${(asset.productStatus.id)!}"; 
			assets.push(asset);
		</#list>
	</@n4.includeScript>

</head>

<div id="steps">
	<div class="step">
		<h2>1. <@s.text name="label.select_event"/></h2>
		<div class="stepContent"  id="step1">
			<#include "_selectEventType.ftl" />
		</div>
		
	</div>

	<div class="step">
		<h2>2. <@s.text name="label.perform_event"/></h2>
		<div class="stepContent"  id="step2">
			
			
		</div>
		<div class="stepContent loader" id="step2Loading" style="display:none">
			${loaderDiv}
			<p>
				<@s.text name="label.retrieving_inspection_info" />
			</p>
		</div>
		
		
	</div>
		<div class="step">
		<h2>3. <@s.text name="label.confirm"/></h2>
		<div class="stepContent"  id="step3">
			<div id="inspectionTypeToReplace"></div>
			
			<div class="stepAction">
				<button id="saveInspections"><@s.text name="label.save_all"/></button>
			<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="backToStep(2)"><@s.text name="label.back_to_step "/> 2</a>
	</div>	
							
			
	</div>
		
	</div>
		<div class="step">
		<h2>4. <@s.text name="label.complete"/></h2>
		<div class="stepContent"  id="step4">
			<div style="overflow:hidden">
				<div style="width:300px; float:left;">
					<@n4.percentbar  progress="0" total="${assets.size()}"/>
				</div>
				<div style="float:left; margin:5px;"><span id="completedInspections">0</span> <@s.text name="label.of"/> ${assets.size()}</div>
			</div>
			
			
			<button id="backToSearchPage" onClick="redirect('<@s.url action="search" namespace="/"/>')" ><@s.text name="label.back_to_search"/></button>
			
		</div>
		
	</div>
</div>