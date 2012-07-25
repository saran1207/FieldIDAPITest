<head>
	<script type="text/javascript" src="<@s.url value="/javascript/asset.js"/>" ></script>
	<script type='text/javascript' src='<@s.url value="/javascript/assetRfidHandler.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateIdentifier.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/combobox.js"/>"></script>
	<script type='text/javascript' src='<@s.url value="/javascript/updateAttributes.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateIdentifier.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/subAsset.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/event.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/masterEvent.js"/>"></script>
	<@n4.includeStyle type="page" href="asset" />
	<@n4.includeStyle type="page" href="subAsset" />
    <@n4.includeStyle href="newCss/event/event_master" />
	<@n4.includeStyle type="page" href="masterEvent" />
		
	<script type="text/javascript">
		lookupAssetUrl = "<@s.url action="assets" namespace="/aHtml"/>";
		assetLookupTitle = "<@s.text name="title.assetlookup"/>";
		subAssetIndex = ${subAssets?size};
		updateAssetTypeUrl = '<@s.url action="assetTypeChange" namespace="ajax"  />';
		addSubAssetUrl = '<@s.url action="createSubAssetInEvent" namespace="/ajax" />';
		addAssetUrl = '<@s.url action="assetAdd" namespace="/ajax"/>';
		createAssetUrl = "<@s.url action="assetCreate" namespace="/ajax" />";	
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		identifierUrl = '<@s.url action="generateIdentifier" namespace="/ajax"  />';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"  />';
		removeSubAssetUrl = "<@s.url action="removeSubAsset" namespace="/ajax"/>";
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
		checkIdentifierUrl = '<@s.url action="checkIdentifier" namespace="/ajax" />';
	</script>

    <script type="text/javascript">

        jQuery(document).ready(function(){
            jQuery('.findExistingLightbox').colorbox({ title: '<@s.text name="title.assetlookup"/>', width: '700px', height: '420px', onComplete: findSubAsset});
        });

    </script>
</head>
${action.setPageType('event', 'add')!}

<div id="masterEvent" >
	<#include "/templates/html/common/_formErrors.ftl" />
	<div class="eventHeader">
		<h3><@s.text name="label.perform_event"/></h3>
		<p class="instructions smallInstructions">
			<@s.text name="instructions.master_event"/>
		</p>
	</div>
	
	<div class="masterAsset <#if masterEvent.mainEventStored >done</#if>">
		<div class="definition"><div class="identifier"><span>${asset.type.name!}</span></div></div>
		<div class="performedEvent">
			<span>${(eventType.name)!}</span>
			<span>
				<a class="exitLink" href="<@s.url action="subEventAdd" uniqueID="0" assetId="${asset.id}" type="${type}" parentAssetId="${asset.id}" token="${token}" scheduleId="${scheduleId!}" />">
					<#if !masterEvent.mainEventStored >
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
			<#include "_subAssetEvent.ftl" />
		</#list>
	</div>
	
	<@s.form action="masterEventCreate" id="subAssetForm" cssClass="crudForm" theme="fieldid">
		<@s.hidden name="uniqueID" id="uniqueID"/>
		<@s.hidden name="token" id="searchToken"/>
		<@s.hidden name="type"/>
		<@s.hidden name="eventGroupId"/>
		<@s.hidden name="assetId" id="assetId"/>
		<@s.hidden name="cleanToEventsToMatchConfiguration" />
		<div class="formAction">
            <@s.url id="cancelUrl" value="w/assetEvents?uniqueID=${asset.id}"/>
			<@s.submit key="label.save" />
			<@s.text name="label.or"/>
			<a href="${cancelUrl}"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
	</div>

<#if sessionUser.hasAccess("tag") >
	<div class="columnSeperator" >
		<@s.text name="label.or"/>
	</div>
	
	<div id="addComponents" class="componentTypes" >
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
						<a href="<@s.url action="assetAdd" namespace="/ajax"  assetTypeId="${type.id}" token="${token}"/>" onclick="addSubAsset(${type.id}, ${(asset.owner.id)}); return false"><@s.text name="label.add_new" /></a> |
						<a href='<@s.url action="assets" namespace="/aHtml"  assetTypeId="${type.id}"/>' class="findExistingLightbox">
				  			<@s.text name="label.find_existing" />
				  		</a>
				  	</div>
			  	</div>
			</div>
		</#list>
	</div>
</#if>



