<head>
    <@n4.includeScript src="myaccount.js"/>
	<@n4.includeStyle href="viewTree"/>
	<@n4.includeStyle href="pageStyles/downloads"/>
	<@n4.includeStyle href="chosen/chosen.css"/>
	<#include "/templates/html/common/_datetimepicker.ftl"/>
			
	<style type="text/css">chzn-select {max-height:110px !important;}</style>
</head>

${action.setPageType('my_account', 'exportEvent')!}
<@s.form id="form" namespace="/aHtml" theme="fieldid" cssClass="crudForm" onsubmit="return exportEvents();">
	
		
	<div style="overflow:visible">
	<#include "../common/_formErrors.ftl"/>
	
		<div class="infoSet" style="overflow:visible; padding: 30px 0px 30px 0px;">
			<label for="eventTypeId" class="label"><@s.text name="label.event_type"/> <#include "../common/_requiredMarker.ftl"/></label>
			<@s.select cssClass="chzn-select" id="eventTypeId" list="eventTypes" name="eventTypeId" listValue="name" listKey="id" label="Event Type"/>
		</div>					

		<div class="infoSet" style="overflow:visible">
			<label for="fromDate"><@s.text name="label.fdate"/></label>
			<@s.textfield  name="fromDate" cssClass="datepicker"/>
		</div>					
		
		<div class="infoSet" style="overflow:visible">
			<label for="toDate"><@s.text name="label.tdate"/></label>
			<@s.textfield  name="toDate" cssClass="datepicker"/>
		</div>
						
		<div style="height:54px"></div>
				
		<div class="formAction borderLessFormAction">
			<@s.submit key="label.exportNow" id="exportButton"/>
			<@s.text name="label.or"/> <a href="<@s.url action="myAccount"/>"><@s.text name="label.cancel"/></a>
		</div>
		
		
	</div>				
	<script type="text/javascript">
		function exportEvents() {
            var exportButton = $('#exportButton');
            exportButton.attr('disabled','disabled');
            exportButton.val('<@s.text name="hbutton.pleasewait"/>');

            var eventTypeId = $('eventTypeId').getValue();
            var from=$('form_fromDate').getValue();
            var to=$('form_toDate').getValue();
            var url = '/fieldid/aHtml/exportEvents.action?from='+from+'&eventTypeId='+eventTypeId+'&to='+to;

            jQuery.get(
                url,
                function(data) {
                     jQuery().colorbox({
						 html: data,
                         onClose: function () {
                             exportButton.val('<@s.text name="label.exportNow"/>');
                             exportButton.removeAttr('disabled');
						 }
					 });
                }
            ) ;
            return false;
        }
		</script>

    <@n4.includeScript src="chosen/chosen.jquery.js"/>

    <script type="text/javascript"> jQuery(document).ready( function() { jQuery(".chzn-select").chosen({ disable_search_threshold: 15 }); } );</script>
				
</@s.form>


