<#-- /templates/ahtml/registerAsset also contains a version of this template -->
<head>
	<script language="javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"> </script>
	<script language="javascript" src="<@s.url value="/javascript/combobox.js"/>"> </script>
	<script type="text/javascript">
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
	</script>
</head>


<div class="assetFormGroup">
	<div class="infoSet">
		<label for="assetStatus" class="label"><@s.text name="label.assetstatus"/></label>
		<#if !parentAsset?exists >
			<@s.select name="assetStatus" list="assetStatuses" listKey="uniqueID" listValue="name" emptyOption="true"  />
		<#else>
			<span class="fieldHolder" id="assetStatus">${(asset.assetStatus.name?html)!}</span>
		</#if>		
	</div>

	<div id="infoOptions">
		<h2><@s.text name="label.asset_details"/></h2>
		<@s.fielderror>
			<@s.param>assetInfoOptions</@s.param>				
		</@s.fielderror>
		
		<#include "_attributes.ftl"/>
	</div>
</div>