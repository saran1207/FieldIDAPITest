<#escape x as x?js_string >
<#assign html>
<@s.select id="eventTypeSelect" name="criteria.eventType" list="eventTypes" listKey="id" listValue="name" emptyOption="true" theme="fieldidSimple"/>
</#assign>
	$('eventTypeSelect').replace('${html}');
</#escape>