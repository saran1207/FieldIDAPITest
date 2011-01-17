${action.setPageType('customer','divisions')!}
<#include "_secondaryNav.ftl"/>
<@s.form action="divisionUpdate" cssClass="fullForm fluidSets" id="mainContent" theme="fieldid">
	<#include "_form.ftl"/>
</@s.form>