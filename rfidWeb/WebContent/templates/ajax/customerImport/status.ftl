<#assign html>
	<div style="width:500px; text-align: center; font-weight: bold;">
		<@s.text name="label.import_status" />: <@s.text name="${task.status.label}" />
	</div>
	
	<div style="width:500px; float:left;">
		<@n4.percentbar progress="${task.currentRow}" total="${task.totalRows}"/>
	</div>
	<div style="float:left; margin:5px;">${task.currentRow} <@s.text name="label.of"/> ${task.totalRows}</div>
</#assign>

$('importStatus').update('${html?js_string}');

<#if importRunning>
	$('uploadForm').disable();
	updateTimer = setTimeout("updateStatus()", 5000);
<#else>
	$('uploadForm').enable();
</#if>