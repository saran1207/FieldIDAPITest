${action.setPageType('inspection_type', 'edit')!}

<@s.url id="cancelUrl" action="inspectionType" uniqueID="${uniqueID}" />
<@s.form action="inspectionTypeUpdate" method="post" theme="simple" cssClass="crudForm largeForm" >
	<#include "_form.ftl"/>
</@s.form>