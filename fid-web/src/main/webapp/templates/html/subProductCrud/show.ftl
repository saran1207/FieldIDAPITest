<head>
	<script type="text/javascript" src="<@s.url value="/javascript/asset.js"/>" ></script>
	<script type='text/javascript' src='<@s.url value="/javascript/productRfidHandler.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateSerialNumber.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/combobox.js"/>"></script>
	<script type='text/javascript' src='<@s.url value="/javascript/updateAttributes.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateSerialNumber.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/subProduct.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/asset.css"/>" />
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/subProduct.css"/>" />
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/masterInspection.css"/>" />
	
	<script type="text/javascript">
		lookupAssetUrl = "<@s.url action="products" namespace="/aHtml"/>";
		assetLookupTitle = "<@s.text name="title.productlookup"/>";
		subAssetIndex = ${subAssets?size};
		updateAssetTypeUrl = '<@s.url action="productTypeChange" namespace="ajax"  />';
		addSubAssetUrl = '<@s.url action="createSubProduct" namespace="/ajax" />';
		addAssetUrl = '<@s.url action="productAdd" namespace="/ajax"/>';
		createAssetUrl = "<@s.url action="productCreate" namespace="/ajax" />";	
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		serialNumberUrl = '<@s.url action="generateSerialNumber" namespace="/aHtml"  />';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"  />';
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';		
		checkSerialNumberUrl = '<@s.url action="checkSerialNumber" namespace="/ajax" />';
		removeSubAssetUrl = '<@s.url action="removeSubProduct" namespace="/ajax"/>';
		labelFormWarning = '<@s.text name="warning.finish_label_editing"/>';
		reorderAssetsUrl = '<@s.url action="productConfigurationUpdateOrder" namespace="/ajax"/>';
		assetIdentifier = 'uniqueID';
	</script>
	
</head>
${action.setPageType('product', 'productconfiguration')!}

	
<@s.hidden name="uniqueID" id="uniqueID"/>
<div id="assetComponents">
	<div class="inspectionHeader">
		<h3><@s.text name="label.orangize_sub_products"/></h3>
		<p class="instructions smallInstructions">
			<@s.text name="instructions.organize_sub_products"/>
		</p>
		<div>
			<a href="#reorder" id="startOrdering" onclick="startOrdering(); return false;"><@s.text name="label.reorder"/></a>
			<a href="#stopOrdering" style="display:none" id="stopOrdering" onclick="stopOrdering(); return false;"><@s.text name="label.done_reordering"/></a>
		</div>
	</div>
	<div id="assetComponentList">
		<#list subAssets as subAsset >
			<#if subAsset?exists >
				<#include "_subProductForm.ftl"/>
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
					<a href="<@s.url action="productAdd" namespace="/ajax"  assetTypeId="${type.id}"/>" id="addSubAsset_${type.id}" onclick="addSubAsset(${type.id}, ${(asset.owner.id)}); return false"><@s.text name="label.add_new" /></a> |
					<a href='<@s.url action="products" namespace="/aHtml"  assetTypeId="${type.id}"/>' id="lookUpSubAsset_${type.id}"  class='lightview' rel='ajax' title='<@s.text name="title.productlookup"/> :: :: scrolling:true, width: 700, height: 420, ajax: { onComplete: findSubAsset }' ><@s.text name="label.find_existing" /></a>
		  		</div> 
			</div>
		</div>
	</#list>
	
</div>


