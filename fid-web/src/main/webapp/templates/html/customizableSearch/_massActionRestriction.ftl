<div id="warning_massUpdate" class="hidden"><@s.text name="warning.max_size_for_mass_update"><@s.param>${maxSizeForMassUpdate}</@s.param></@s.text></div>
<div id="warning_exportToExcel" class="hidden" ><@s.text name="warning.max_size_for_export_to_excel"><@s.param>${maxSizeForExcelExport}</@s.param></@s.text></div>
<div id="warning_printAllPDFs" class="hidden" ><@s.text name="warning.max_size_for_pdf_print_outs"><@s.param>${maxSizeForPDFPrintOuts}</@s.param></@s.text></div>
<div id="warning_summaryReport" class="hidden" ><@s.text name="warning.max_size_for_summary_report"><@s.param>${maxSizeForSummaryReport}</@s.param></@s.text></div>
<div id="warning_multi_event" class="hidden" ><@s.text name="warning.max_size_for_multi_event"><@s.param>${maxSizeForMultiEvent}</@s.param></@s.text></div>
<div id="warning_assignEventsToJob" class="hidden" ><@s.text name="warning.max_size_for_assigning_events_to_job"><@s.param>${maxSizeForAssigningEventsToJobs}</@s.param></@s.text></div>
<div id="warning_massSchedule" class="hidden" ><@s.text name="warning.max_size_for_mass_schedule"><@s.param>${maxSizeForMassSchedule}</@s.param></@s.text></div>
<div id="warning_nothingSelected" class="hidden" ><@s.text name="warning.nothing_selected"/></div>

<script type="text/javascript">
    var maxSizeForMassUpdate = ${maxSizeForMassUpdate};
    var maxSizeForExcelExport = ${maxSizeForExcelExport};
    var maxSizeForPDFPrintOuts = ${maxSizeForPDFPrintOuts};
    var maxSizeForSummaryReport = ${maxSizeForSummaryReport};
    var maxSizeForMultiEvent = ${maxSizeForMultiEvent};
    var maxSizeForAssigningEventsToJobs = ${maxSizeForAssigningEventsToJobs};
    var maxSizeForMassSchedule = ${maxSizeForMassSchedule};
    var totalResults = ${totalResults};

    function addGuardForTooManySelectedToElements(cssSelector, maxSelected, warningDiv) {
        $$(cssSelector).each(function(element) {
            element.observe('click', function(event) {
                if (numSelectedItems() == 0) {
                    Event.stop(event); showQuickView("warning_nothingSelected", event);
                }
                else if (numSelectedItems() > maxSelected) {
                    Event.stop(event); showQuickView(warningDiv, event);
                }
            });
        });
    }

    addGuardForTooManySelectedToElements(".massUpdate", maxSizeForMassUpdate, 'warning_massUpdate');
    addGuardForTooManySelectedToElements(".exportToExcel", maxSizeForExcelExport, 'warning_exportToExcel');
    addGuardForTooManySelectedToElements(".printAllPDFs", maxSizeForPDFPrintOuts, 'warning_printAllPDFs');
    addGuardForTooManySelectedToElements(".multiEvent", maxSizeForMultiEvent, 'warning_multi_event');
    addGuardForTooManySelectedToElements(".assignEventsToJob", maxSizeForAssigningEventsToJobs, 'warning_assignEventsToJob');
	addGuardForTooManySelectedToElements(".summaryReport", maxSizeForSummaryReport, 'warning_summaryReport');
    addGuardForTooManySelectedToElements(".massSchedule", maxSizeForMassSchedule, 'warning_massSchedule');

    if (totalResults > maxSizeForSummaryReport) {
        $$(".summaryReport").each(function(element) {
            element.observe('click', function(event) {
                Event.stop(event); showQuickView('warning_summaryReport', event);
            });
        });
    }

</script>

