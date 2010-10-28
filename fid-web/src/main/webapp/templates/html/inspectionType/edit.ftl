${action.setPageType('inspection_type', 'edit')!}

<@s.url id="cancelUrl" action="eventType" uniqueID="${uniqueID}" />
<@s.form action="eventTypeUpdate" method="post" theme="simple" cssClass="crudForm largeForm" >
	<#include "_form.ftl"/>
</@s.form>