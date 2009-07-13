<title><@s.text name="title.assigntojob" /></title>

<@s.form action="assignInspectionsToJob" theme="fieldid" cssClass="crudForm">
	<#include "_form.ftl"/>
	<div class="formAction">
		<a href="<@s.url action="reportResults" searchId="${searchId!1}" currentPage="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
		<@s.submit key="hbutton.save" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
	</div>
</@s.form>
