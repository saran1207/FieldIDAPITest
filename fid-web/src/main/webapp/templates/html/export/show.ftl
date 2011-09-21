<head>
	<script type="text/javascript" src="<@s.url value="/javascript/myaccount.js"/>"></script>
	<@n4.includeStyle href="viewTree"/>
	<@n4.includeStyle href="pageStyles/downloads"/>
	<#include "/templates/html/common/_datetimepicker.ftl"/>	
</head>

${action.setPageType('my_account', 'exportEvent')!}
<@s.form id="form" namespace="/aHtml" theme="fieldid" cssClass="crudForm" onsubmit="return exportEvents();">
	<#include "../common/_formErrors.ftl"/>
		<div class="infoSet">
			<label for="eventTypeId" class="label"><@s.text name="label.event_type"/> <#include "../common/_requiredMarker.ftl"/></label>
			<@s.select id="eventTypeId" list="eventTypes" name="eventTypeId" listValue="name" listKey="id" label="Event Type"/>
		</div>					

		<div class="infoSet">
			<label for="fromDate"><@s.text name="label.fdate"/></label>
			<@s.textfield  name="fromDate" cssClass="datepicker"/>
		</div>					
		
		<div class="infoSet">
			<label for="toDate"><@s.text name="label.tdate"/></label>
			<@s.textfield  name="toDate" cssClass="datepicker"/>
		</div>
						
		<div class="formAction borderLessFormAction">
			<@s.submit theme="ajax" key="label.exportNow" id="exportButton"/>
			<@s.text name="label.or"/> <a href="<@s.url action="myAccount"/>"><@s.text name="label.cancel"/></a>
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
</@s.form>


