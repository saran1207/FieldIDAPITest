<#escape x as x?js_string >
<#assign html>
<@s.select cssClass="eventTypeSelect" name="view.eventTypeId" emptyOption="true" list="eventTypes" listKey="id" listValue="name" theme="fieldidSimple"/>
</#assign>
	$$('.eventTypeSelect').each(function(element) {
		element.replace('${html}');
    });
</#escape>
