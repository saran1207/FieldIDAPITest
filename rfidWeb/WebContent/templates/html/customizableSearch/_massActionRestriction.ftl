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





<#if maxSizeForAssigningInspectionsToJobs lt totalResults>
	<div id="warning_assignInspectionsToJob" class="hidden" ><@s.text name="warning.max_size_for_assigning_inspectiosn_to_job"><@s.param>${maxSizeForAssigningInspectionsToJobs}</@s.param></@s.text></div>
	<script type="text/javascript">
		var assignToJob = $$(".assignInspectionsToJob");
		assignToJob.each(function(element, index) { element.addClassName("disabled"); element.removeClassName("lightview"); element.writeAttribute('title'); element.observe('click', function(event) { Event.stop(event); showQuickView('warning_assignInspectionsToJob', event); } ); } );
		
	</script>
</#if>