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
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/asset.css"/>" />
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/subAsset.css"/>" />
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/masterInspection.css"/>" />
	
	<script type="text/javascript">
		lookupAssetUrl = "<@s.url action="assets" namespace="/aHtml"/>";
		assetLookupTitle = "<@s.text name="title.assetlookup"/>";
		subAssetIndex = ${subAssets?size};
		updateAssetTypeUrl = '<@s.url action="assetTypeChange" namespace="ajax"  />';
		addSubAssetUrl = '<@s.url action="createSubAsset" namespace="/ajax" />';
		addAssetUrl = '<@s.url action="assetAdd" namespace="/ajax"/>';
		createAssetUrl = "<@s.url action="assetCreate" namespace="/ajax" />";	
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		serialNumberUrl = '<@s.url action="generateSerialNumber" namespace="/aHtml"  />';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"  />';
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';		
		checkSerialNumberUrl = '<@s.url action="checkSerialNumber" namespace="/ajax" />';
		removeSubAssetUrl = '<@s.url action="removeSubAsset" namespace="/ajax"/>';
		labelFormWarning = '<@s.text name="warning.finish_label_editing"/>';
		reorderAssetsUrl = '<@s.url action="assetConfigurationUpdateOrder" namespace="/ajax"/>';
		assetIdentifier = 'uniqueID';
	</script>
	
</head>
${action.setPageType('asset', 'assetconfiguration')!}

	
<@s.hidden name="uniqueID" id="uniqueID"/>
<div id="assetComponents">
	<div class="inspectionHeader">
		<h3><@s.text name="label.orangize_sub_assets"/></h3>
		<p class="instructions smallInstructions">
			<@s.text name="instructions.organize_sub_assets"/>
		</p>
		<div>
			<a href="#reorder" id="startOrdering" onclick="startOrdering(); return false;"><@s.text name="label.reorder"/></a>
			<a href="#stopOrdering" style="display:none" id="stopOrdering" onclick="stopOrdering(); return false;"><@s.text name="label.done_reordering"/></a>
		</div>
	</div>
	<div id="assetComponentList">
		<#list subAssets as subAsset >
			<#if subAsset?exists >
				<#include "_subAssetForm.ftl"/>
			</#if>
		</#list>
	</div>
	
	
</div>

	
<div class="columnSeperator notAllowedDuringOrdering" >
	<@s.text name="label.or"/>
</div>


<div id="addComponents" class="componentTypes notAllowedDuringOrdering" >
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
					<a href="<@s.url action="assetAdd" namespace="/ajax"  assetTypeId="${type.id}"/>" id="addSubAsset_${type.id}" onclick="addSubAsset(${type.id}, ${(asset.owner.id)}); return false"><@s.text name="label.add_new" /></a> |
					<a href='<@s.url action="assets" namespace="/aHtml"  assetTypeId="${type.id}"/>' id="lookUpSubAsset_${type.id}"  class='lightview' rel='ajax' title='<@s.text name="title.assetlookup"/> :: :: scrolling:true, width: 700, height: 420, ajax: { onComplete: findSubAsset }' ><@s.text name="label.find_existing" /></a>
		  		</div> 
			</div>
		</div>
	</#list>
	
</div>


