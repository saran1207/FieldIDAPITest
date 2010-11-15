<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/assetTypeConfiguration.css"/>" />
	<script type="text/javascript" src="<@s.url value="/javascript/assets/assetTypeConfiguration.js" />" ></script>
	<script type="text/javascript">
		numberOfSubTypes = ${subAssetIds?size};
		removeString = '<@s.text name="label.remove"/>';
	</script>
</head>

${action.setPageType('asset_type', 'configuration')!}
<#if partOfMasterAsset >
	<div class="formErrors error" >
		 <@s.text name="instruction.typealreadyusedbyparent"><@s.param >${assetType.name}</@s.param></@s.text>
	</div>
		
<#else>
	<div class="instruction">
		<h3><span class="instructionTitle"><@s.text name="label.tellushowtoconfigure"/>&nbsp;</span><span>${assetType.name}</span></h3>
		<p >
			<@s.text name="instruction.assetconfiguration">
				<@s.param>${assetType.name}</@s.param>
			</@s.text>
		</p>
	</div>
	<@s.form action="assetTypeConfigurationUpdate" id="assetTypeConfigurationUpdate" theme="fieldidSimple" cssClass="crudForm">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<@s.hidden name="uniqueID" />
		<h3><@s.text name="label.A"/> ${assetType.name} <@s.text name="label.has" />: </h3>
		<ul id="subAssets">
			<#list subAssets as subAsset >
				<li id="subAsset_${subAsset.id}">
					<@s.hidden name="subAssetIds[${subAsset_index}]" value="${subAsset.id}" />
					<span id="assetName_${subAsset.id}">${subAsset.name}</span>
					<a id="removeAssetLink_${subAsset.id}" href="removeSubAsset" assetTypeId="${subAsset.id}"  ><@s.text name="label.remove"/></a>
					<script type="text/javascript" >
						$('removeAssetLink_${subAsset.id}').observe('click', removeAssetEvent);
					</script>
				</li>
			</#list>
		</ul>
		<div id="addSubAssetContainer">
			<span> <@s.select id="addSubAsset" name="addSubAsset" list="assetTypes" listKey="id" listValue="name"  /></span>
			<@s.submit id="addSubAssetButton" key="label.add" type="button" />
		</div>
		
		<div class="formAction">
			<@s.submit key="label.save"/>
			<@s.url id="cancelUrl" action="assetType" uniqueID="${uniqueID}"/>
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.cancel"/></a>
		</div>
	
	</@s.form>
		<script type="text/javascript" >
			$('addSubAssetButton').observe('click', includeSubAsset);
		</script>
</#if>