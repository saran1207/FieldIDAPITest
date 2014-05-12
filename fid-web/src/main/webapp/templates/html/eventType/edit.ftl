${action.setPageType('event_type', 'edit')!}
<head>
<@n4.includeStyle href="/style/legacy/newCss/component/buttons.css" type="page"/>
</head>

<@s.url id="cancelUrl" action="eventType" uniqueID="${uniqueID}" />
<@s.form action="eventTypeUpdate" method="post" theme="simple" cssClass="crudForm largeForm" >
	<#include "_form.ftl"/>
</@s.form>