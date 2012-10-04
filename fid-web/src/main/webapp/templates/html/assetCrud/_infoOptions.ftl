<#-- /templates/ahtml/registerAsset also contains a version of this template -->
<head>
    <@n4.includeScript src="unitOfMeasure.js"/>
    <@n4.includeScript src="combobox.js"/>

	<script type="text/javascript">
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
	</script>
</head>

<div id="infoOptions">
	<@s.fielderror>
		<@s.param>assetInfoOptions</@s.param>				
	</@s.fielderror>
	
	<#include "_attributes.ftl"/>
</div>
