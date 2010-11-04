${action.setPageType('event_type', 'add')!}
<@s.form action="eventTypeCreate" method="post" theme="simple" cssClass="crudForm largeForm" >
	<@s.url id="cancelUrl" action="eventTypes" includeParams="none" />
	<#include "_form.ftl"/>
</@s.form>