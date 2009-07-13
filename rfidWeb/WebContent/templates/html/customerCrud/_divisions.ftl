<head>
	<script language="javascript" src="javascript/divisionCrud.js"></script>
</head>
<div id="divisionEditor">
<#if divisions ? exists>
<div id="divisionContainer">
<@s.iterator value="divisions" id="division" status="stat" theme="simple">
	<input name="division_${division.id}" id="division_${division.id}" value="${division.name}" size="60" /><@s.a href="javascript: void(0);" id="updateDiv_${division.id}" onclick="updateDivision(${division.id});"><@s.text name="label.update" /></@s.a> <@s.a href="javascript: void(0);" id="deleteDiv_${division.id}" onclick="deleteDivision(${division.id});"><@s.text name="hbutton.delete" /></@s.a><div id="divisionMessage_${division.id}" class="error"></div>
</@s.iterator>
</div>

<div style="display:none">
<input id="divisionStandby" size="60" /><a href="javascript:void(0);" id="updateStandby"><@s.text name="label.update" /></a> <a href="javascript:void(0);" id="deleteStandby"><@s.text name="hbutton.delete" /></a> <span id="divisionMessageStandby" class="error">&nbsp;</span>
</div>
</#if>
<br />

<input name="newDivisionName" id="newDivisionName" size="60" /><input type="button" value="<@s.text name="hbutton.adddivision" />" onclick="addDivision();" /><span id="divisionMessage" class="error">&nbsp;</span>
</div>

<script langauge="javascript">
addURL = '<@s.url action="divisionChange" method="add" namespace="ajax"/>';
updateURL = '<@s.url action="divisionChange" method="update" namespace="ajax"/>';
deleteURL = '<@s.url action="divisionChange" method="delete" namespace="ajax"/>';
uniqueID = ${uniqueID};
</script>

