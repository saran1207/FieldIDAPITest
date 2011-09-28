<head>
	<script type="text/javascript" src="<@s.url value="/javascript/myaccount.js"/>"></script>
	<@n4.includeStyle href="viewTree"/>
	<@n4.includeStyle href="pageStyles/downloads"/>
	<@n4.includeStyle href="chosen/chosen.css"/>
	<@n4.includeScript src="jquery-1.4.2.min.js"/>
	<@n4.includeScript>jQuery.noConflict();</@n4.includeScript>	
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
			<@s.submit theme="ajax" key="label.exportNow" id="exportButton"/>
			<@s.text name="label.or"/> <a href="<@s.url action="myAccount"/>"><@s.text name="label.cancel"/></a>
		</div>
		
		
	</div>				
	<script type="text/javascript">
		function exportEvents() {			
				var eventTypeId = $('eventTypeId').getValue();  
				var from=$('form_fromDate').getValue();
				var to=$('form_toDate').getValue();				 				
				var url = '/fieldid/aHtml/exportEvents.action?from='+from+'&eventTypeId='+eventTypeId+'&to='+to;
					Lightview.show(	
						{	href: url,
							rel: 'ajax',
							title: '',
							options: { autosize : true }							
						});
						return false;
					}
		</script>
		
		<script src="javascript/chosen/chosen.jquery.js" type="text/javascript"></script>
		<script type="text/javascript"> jQuery(document).ready( function() { jQuery(".chzn-select").chosen(); } );</script>
				
</@s.form>


