<head>
	<script language="javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"> </script>
	<script language="javascript" src="<@s.url value="/javascript/combobox.js"/>"> </script>
	<script type="text/javascript">
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
	</script>
</head>
<#if assetCodeMapping.uniqueID?exists>
	${action.setPageType('asset_code_mapping','edit')!}
<#else>
	${action.setPageType('asset_code_mapping','add')!}
</#if>
<@s.form action="assetCodeMappingEdit!save" cssClass="fullForm" theme="fieldid" >
	<@s.hidden name="uniqueID"/>
	<div class="fluidSets">
		<div class="infoSet">
			<label class="label" for="assetCode"><@s.text name="label.assetcode"/></label>
			<@s.textfield  name="assetCode"/>
		</div>
		<div class="infoSet">
			<label class="label" for="customerRefNumber"><@s.text name="label.referencenumber"/></label>
			<@s.textfield name="customerRefNumber" />
		</div>
		<div class="infoSet">
			<input type="hidden" name="assetTypeUpdate" value="false" id="assetTypeUpdate" />
			<label class="label" for="assetType"><@s.text name="label.assettype"/></label>
			<@s.select id="assetType" name="assetType" emptyOption="true" onchange="updateAssetType(this)">
				<#include "/templates/html/common/_assetTypeOptions.ftl"/>
			</@s.select>
		</div>	
		
		<div id="infoOptions">
			<#if assetType?exists >
				<#assign fieldPrefix='asset' />
				<#assign prefix='asset'/>
				<#assign requires=false>
				<#include "/templates/html/assetCrud/_attributes.ftl" />
			</#if>
		</div>
	</div>
	<div class="actions">
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="assetCodeMappingList"/>"><@s.text name="label.cancel" /></a>
	</div>
</@s.form >
<script type="text/javascript">
	function updateAssetType( assetTypeSelect ) {
		assetTypeSelect.form.assetTypeUpdate.value = "true";
		assetTypeSelect.form.action = '<@s.url action="assetCodeMappingEdit" includeParams="none"/>';
		assetTypeSelect.form.submit();
		assetTypeSelect.form.assetTypeUpdate.value = "false";
		assetTypeSelect.form.action = '<@s.url action="assetCodeMappingEdit!save" includeParams="none"/>';
	}
</script>