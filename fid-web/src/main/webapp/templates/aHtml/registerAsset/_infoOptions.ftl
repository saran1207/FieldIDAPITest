<#-- /templates/html/assetCrud also contains a version of this template -->

<head>
	<script language="javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"> </script>
	<script language="javascript" src="<@s.url value="/javascript/combobox.js"/>"> </script>
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
