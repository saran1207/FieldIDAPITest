<#escape x as x?js_string >
<#assign html>
<@s.select cssClass="eventTypeSelect" id="eventType" name="criteria.eventType" list="eventTypes" listKey="id" listValue="name" emptyOption="true" theme="fieldidSimple" onchange="eventTypeChanged(this)" />
</#assign>
	$$('.eventTypeSelect').each(function(element) {
        element.replace('${html}');
    });
    eventTypeChanged($('eventType'));
</#escape>