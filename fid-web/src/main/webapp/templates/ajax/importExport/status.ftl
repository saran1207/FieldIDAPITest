<#assign html>
	<div>
		<h2><@s.text name="label.import_status" /></h2>
	</div>
	
	<div class="progressBarContainer">
		<label class="statusLabel"><@s.text name="${task.status.label}" /></label>
		<@n4.percentbar progress="${task.currentRow}" total="${task.totalRows}"/>
		<div class="taskCount">${task.currentRow} <@s.text name="label.of"/> ${task.totalRows}</div>
		<div class="importMessage"><@s.text name="label.import_status_message"/></div>
	</div>
</#assign>

$('importStatus').update('${html?js_string}');

<#if importRunning>
	
	if( $('importStatus') != null) {
		updateTimer = setTimeout("updateStatus()", 5000);
	}
	
</#if>