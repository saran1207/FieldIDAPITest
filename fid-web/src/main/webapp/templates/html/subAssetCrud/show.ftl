<head>
    <@n4.includeScript src="asset.js"/>
    <@n4.includeScript src="assetRfidHandler.js"/>
    <@n4.includeScript src="generateIdentifier.js"/>
    <@n4.includeScript src="lockSubmitButtons.js"/>
    <@n4.includeScript src="unitOfMeasure.js"/>
    <@n4.includeScript src="combobox.js"/>
    <@n4.includeScript src="updateAttributes.js"/>
    <@n4.includeScript src="generateIdentifier.js"/>
    <@n4.includeScript src="lockSubmitButtons.js"/>
    <@n4.includeScript src="subAsset.js"/>

    <@n4.includeStyle type="page" href="pageStyles/asset.css"/>
    <@n4.includeStyle type="page" href="pageStyles/subAsset.css"/>
    <@n4.includeStyle type="page" href="pageStyles/masterEvent.css"/>


	<script type="text/javascript">
		lookupAssetUrl = "<@s.url action="assets" namespace="/aHtml"/>";
		assetLookupTitle = "<@s.text name="title.assetlookup"/>";
		subAssetIndex = ${subAssets?size};
		updateAssetTypeUrl = '<@s.url action="assetTypeChange" namespace="ajax"  />';
		addSubAssetUrl = '<@s.url action="createSubAsset" namespace="/ajax" />';
		addAssetUrl = '<@s.url action="assetAdd" namespace="/ajax"/>';
		createAssetUrl = "<@s.url action="assetCreate" namespace="/ajax" />";	
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		identifierUrl = '<@s.url action="generateIdentifier" namespace="/ajax"  />';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"  />';
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';		
		checkIdentifierUrl = '<@s.url action="checkIdentifier" namespace="/ajax" />';
		removeSubAssetUrl = '<@s.url action="removeSubAsset" namespace="/ajax"/>';
		labelFormWarning = '<@s.text name="warning.finish_label_editing"/>';
		reorderAssetsUrl = '<@s.url action="assetConfigurationUpdateOrder" namespace="/ajax"/>';
		assetIdentifier = 'uniqueID';
	</script>

    <script type="text/javascript">

        jQuery(document).ready(function(){
            jQuery('.findExistingLightbox').colorbox({ title: '<@s.text name="title.assetlookup"/>', width: '700px', height: '420px', onComplete: findSubAsset});
        });

    </script>
	
</head>
${action.setPageType('asset', 'assetconfiguration')!}

	
<@s.hidden name="uniqueID" id="uniqueID"/>
<div id="assetComponents">
	<div class="eventHeader">
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
	<div class="eventHeader">
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
					<a href='<@s.url action="assets" namespace="/aHtml"  assetTypeId="${type.id}"/>' id="lookUpSubAsset_${type.id}"  class="findExistingLightbox">
                        <@s.text name="label.find_existing" />
                    </a>
		  		</div> 
			</div>
		</div>
	</#list>
	
</div>


