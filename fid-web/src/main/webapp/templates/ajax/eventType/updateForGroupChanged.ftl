<#escape x as x?js_string >
<#assign html>
<@s.select cssClass="eventTypeSelect" name="criteria.eventType" list="eventTypes" listKey="id" listValue="name" emptyOption="true" theme="fieldidSimple"/>
</#assign>
	$$('.eventTypeSelect').each(function(element) {
        element.replace('${html}');
    });
</#escape>