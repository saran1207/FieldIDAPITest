<#-- /templates/ahtml/registerAsset also contains a version of this template -->
<head>
	<script language="javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"> </script>
	<script language="javascript" src="<@s.url value="/javascript/combobox.js"/>"> </script>
	<script type="text/javascript">
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
	</script>
</head>


<div class="assetFormGroup">
	<h2><@s.text name="label.asset_details"/></h2>
	<div class="infoSet">
		<label for="assetStatus" class="label"><@s.text name="label.assetstatus"/></label>
		<#if !parentAsset?exists >
			<@s.select name="assetStatus" list="assetStatuses" listKey="uniqueID" listValue="name" emptyOption="true"  />
		<#else>
			<span class="fieldHolder" id="assetStatus">${(asset.assetStatus.name?html)!}</span>
		</#if>		
	</div>

	<div id="infoOptions">
	
		<@s.fielderror>
			<@s.param>assetInfoOptions</@s.param>				
		</@s.fielderror>
		
		<#include "_attributes.ftl"/>
	</div>
	
	<div class="infoSet">
		<label for="comments" class="label"><@s.text name="label.comments"/></label>
		<span class="fieldHolder">
			<@s.select id="commentTemplateSelection" name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/><br/>
			<@s.textarea id="comments"  name="comments" theme="fieldidSimple"/>
		</span>
	</div>
</div>