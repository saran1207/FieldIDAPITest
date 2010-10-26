<head>
	<script type="text/javascript" src="<@s.url value="/javascript/asset.js"/>" ></script>
	<script type='text/javascript' src='<@s.url value="/javascript/assetRfidHandler.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateSerialNumber.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/combobox.js"/>"></script>
	<script type='text/javascript' src='<@s.url value="/javascript/updateAttributes.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateSerialNumber.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/subAsset.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/inspection.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/masterInspection.js"/>"></script>
	<@n4.includeStyle type="page" href="asset" />
	<@n4.includeStyle type="page" href="subAsset" />
	<@n4.includeStyle type="page" href="inspection" />
	<@n4.includeStyle type="page" href="masterInspection" />
	
	<#include "/templates/html/common/_calendar.ftl"/>
	
	<script type="text/javascript">
		lookupAssetUrl = "<@s.url action="assets" namespace="/aHtml"/>";
		assetLookupTitle = "<@s.text name="title.assetlookup"/>";
		subAssetIndex = ${subAssets?size};
		updateAssetTypeUrl = '<@s.url action="assetTypeChange" namespace="ajax"  />';
		addSubAssetUrl = '<@s.url action="createSubAssetInInspection" namespace="/ajax" />';
		addAssetUrl = '<@s.url action="assetAdd" namespace="/ajax"/>';
		createAssetUrl = "<@s.url action="assetCreate" namespace="/ajax" />";	
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		serialNumberUrl = '<@s.url action="generateSerialNumber" namespace="/aHtml"  />';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"  />';
		removeSubAssetUrl = "<@s.url action="removeSubAsset" namespace="/ajax"/>";
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
		checkSerialNumberUrl = '<@s.url action="checkSerialNumber" namespace="/ajax" />';
		
	</script>
</head>
${action.setPageType('inspection', 'add')!}

<div id="masterInspection" >
	<#include "/templates/html/common/_formErrors.ftl" />
	<div class="inspectionHeader">
		<h3><@s.text name="label.perform_event"/></h3>
		<p class="instructions smallInstructions">
			<@s.text name="instructions.master_inspection"/>
		</p>
	</div>
	
	<div class="masterAsset <#if masterInspection.mainInspectionStored >done</#if>">
		<div class="definition"><div class="identifier"><span>${asset.type.name!}</span></div></div>
		<div class="performedInspection">
			<span>${(inspectionType.name)!}</span> 
			<span>
				<a class="exitLink" href="<@s.url action="subInspectionAdd" uniqueID="0" assetId="${asset.id}" type="${type}" parentAssetId="${asset.id}" token="${token}" scheduleId="${scheduleId!}" />">
					<#if !masterInspection.mainInspectionStored >
						<@s.text name="label.you_must_perform_this_event"/>
					<#else>
						<@s.text name="label.edit_this_event"/>
					</#if>
				</a>
			</span>
		</div>
	</div>

	<div id="assetComponents">
		<#list asset.subAssets as subAsset >
			<#include "_subAssetInspection.ftl" />
		</#list>
	</div>
	
	<@s.form action="masterInspectionCreate" id="subAssetForm" cssClass="crudForm" theme="fieldid">
		<@s.hidden name="uniqueID" id="uniqueID"/>
		<@s.hidden name="token" id="searchToken"/>
		<@s.hidden name="type"/>
		<@s.hidden name="inspectionGroupId"/>
		<@s.hidden name="assetId" id="assetId"/>
		<@s.hidden name="cleanToInspectionsToMatchConfiguration" />
		<div class="formAction">
			<@s.url id="cancelUrl" action="inspectionGroups" uniqueID="${asset.id}"/>
			<@s.submit key="label.cancel" onclick="return redirect( '${cancelUrl}' );"/>
			<@s.submit key="label.save" />
		</div>
	</@s.form>
	</div>

<#if sessionUser.hasAccess("tag") >
	<div class="columnSeperator" >
		<@s.text name="label.or"/>
	</div>
	
	<div id="addComponents" class="componentTypes" >
		<div class="inspectionHeader">
			<h3><@s.text name="label.add_new_component"/></h3>
			<p class="instructions smallInstructions">
				<@s.text name="instructions.add_new_component"/>
			</p>
		</div>
		
		<#list subTypes?sort_by('name') as type >
			<div id="subAssetType_${type.id}" class="component"> 
				<div class="definition">
					<div class="identifier">${type.name}</div> 
					<div class="createOptions">
						<a href="<@s.url action="assetAdd" namespace="/ajax"  assetTypeId="${type.id}" token="${token}"/>" onclick="addSubAsset(${type.id}, ${(asset.owner.id)}); return false"><@s.text name="label.add_new" /></a> |
						<a href='<@s.url action="assets" namespace="/aHtml"  assetTypeId="${type.id}"/>'  class='lightview' rel='ajax' title='<@s.text name="title.assetlookup"/> :: :: scrolling:true, width: 700, height: 420, ajax: { onComplete: findSubAsset }' >
				  			<@s.text name="label.find_existing" />
				  		</a>
				  	</div>
			  	</div>
			</div>
		</#list>
	</div>
</#if>



