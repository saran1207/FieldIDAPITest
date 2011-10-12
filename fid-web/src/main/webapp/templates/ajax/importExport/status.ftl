<#assign html>
	<div style="width:500px; text-align: center; font-weight: bold; padding-bottom: 10px;">
		<h2><@s.text name="label.import_status" /></h2>
		<@s.text name="${task.status.label}" />
	</div>
	
	<div style="width:500px; float:left;">
		<@n4.percentbar progress="${task.currentRow}" total="${task.totalRows}"/>
	</div>
	<div style="float:left; margin:5px;">${task.currentRow} <@s.text name="label.of"/> ${task.totalRows}</div>
</#assign>

$('importStatus').update('${html?js_string}');

<#if importRunning>
	
	if( $('uploadForm') != null) {
		$('uploadForm').disable();
		updateTimer = setTimeout("updateStatus()", 5000);
	}
<#else>
	if( $('uploadForm') != null) {
		$('uploadForm').enable();
	}
</#if>