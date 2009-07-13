${action.setPageType('inspection_type', 'add')!}
<@s.form action="inspectionTypeCreate" method="post" theme="simple" cssClass="crudForm largeForm" >
	<@s.url id="cancelUrl" action="inspectionTypes" includeParams="none" />
	<#include "_form.ftl"/>
</@s.form>