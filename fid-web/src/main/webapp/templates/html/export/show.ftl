<head>
	<script type="text/javascript" src="<@s.url value="/javascript/myaccount.js"/>"></script>
	<@n4.includeStyle href="viewTree"/>
	<#include "/templates/html/common/_datetimepicker.ftl"/>	
</head>

${action.setPageType('my_account', 'exportEvent')!}
<@s.form id="form" action="exportEventUpdate" cssClass="fullForm contentBlock" theme="fieldid">
	<#include "../common/_formErrors.ftl"/>
	<div class="container">
		<div class="infoSet infoBlock">
			<label for="eventTypeId" class="label"><@s.text name="label.event_type"/> <#include "../common/_requiredMarker.ftl"/></label>
			<@s.select id="eventTypeId" list="eventTypes" name="eventTypeId" listValue="name" listKey="id" label="Event Type"/>
		</div>					
	</div>
	
	<div class="container">
		<div class="infoSet">
			<label for="fromDate"><@s.text name="label.fdate"/></label>
			<@s.textfield  name="fromDate" cssClass="datepicker"/>
		</div>
		<div class="infoSet">
			<label for="toDate"><@s.text name="label.tdate"/></label>
			<@s.textfield  name="toDate" cssClass="datepicker"/>
		</div>
	</div>
		
	<div class="formAction borderLessFormAction">
		<@s.submit key="label.save" id="saveButton"/>
		<@s.text name="label.or"/> <a href="<@s.url action="myAccount"/>"><@s.text name="label.cancel"/></a>
	</div>
	
</@s.form>


