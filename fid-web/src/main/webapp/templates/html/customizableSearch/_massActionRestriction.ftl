<#if maxSizeForMassUpdate lt totalResults >
	<div id="warning_massUpdate" class="hidden"><@s.text name="warning.max_size_for_mass_update"><@s.param>${maxSizeForMassUpdate}</@s.param></@s.text></div>
	<script type="text/javascript">
		var massUpdates = $$(".massUpdate");
		massUpdates.each(function(element, index) { element.addClassName("disabled"); element.removeClassName("lightview"); element.writeAttribute('title'); element.observe('click', function(event) { Event.stop(event); showQuickView('warning_massUpdate', event);  } ); } );
	</script>
</#if>

<#if maxSizeForExcelExport lt totalResults >
	<div id="warning_exportToExcel" class="hidden" ><@s.text name="warning.max_size_for_export_to_excel"><@s.param>${maxSizeForExcelExport}</@s.param></@s.text></div>
	<script type="text/javascript">
		var exportExcels = $$(".exportToExcel");
		exportExcels.each(function(element, index) { element.addClassName("disabled");  element.removeClassName("lightview"); element.writeAttribute('title'); element.observe('click', function(event) { Event.stop(event); showQuickView('warning_exportToExcel', event); } ); } );
	</script>
</#if>


<#if maxSizeForPDFPrintOuts lt totalResults>
	<div id="warning_printAllPDFs" class="hidden" ><@s.text name="warning.max_size_for_pdf_print_outs"><@s.param>${maxSizeForPDFPrintOuts}</@s.param></@s.text></div>
	<script type="text/javascript">
		var printAllPDFs = $$(".printAllPDFs");
		printAllPDFs.each(function(element, index) { element.addClassName("disabled"); element.removeClassName("lightview"); element.writeAttribute('title'); element.observe('click', function(event) { Event.stop(event); showQuickView('warning_printAllPDFs', event); } ); } );
	</script>
</#if>


<#if maxSizeForSummaryReport lt totalResults>
	<div id="warning_summaryReport" class="hidden" ><@s.text name="warning.max_size_for_summary_report"><@s.param>${maxSizeForSummaryReport}</@s.param></@s.text></div>
	<script type="text/javascript">
		var summaryReports = $$(".summaryReport");
		summaryReports.each(function(element, index) { element.addClassName("disabled"); element.removeClassName("lightview"); element.writeAttribute('title'); element.observe('click', function(event) { Event.stop(event);  showQuickView('warning_summaryReport', event); } ); } );
	</script>	
</#if>


<#if maxSizeForMultiEvent lt totalResults >
	<div id="warning_multi_event" class="hidden" ><@s.text name="warning.max_size_for_multi_event"><@s.param>${maxSizeForMultiEvent}</@s.param></@s.text></div>
	<script type="text/javascript">
		var multiEvents = $$(".multiEvent");
		multiEvents.each(function(element, index) { element.addClassName("disabled"); element.observe('click', function(event) { Event.stop(event);  showQuickView('warning_multi_event', event); } ); } );
	</script>	
</#if>

<#if maxSizeForAssigningEventsToJobs lt totalResults>
	<div id="warning_assignEventsToJob" class="hidden" ><@s.text name="warning.max_size_for_assigning_events_to_job"><@s.param>${maxSizeForAssigningEventsToJobs}</@s.param></@s.text></div>
	<script type="text/javascript">
		var assignToJob = $$(".assignEventsToJob");
		assignToJob.each(function(element, index) { element.addClassName("disabled"); element.removeClassName("lightview"); element.writeAttribute('title'); element.observe('click', function(event) { Event.stop(event); showQuickView('warning_assignEventsToJob', event); } ); } );
		
	</script>
</#if>
