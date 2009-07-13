<title><@s.text name="title.assigntojob" /></title>

<@s.form action="assignToJob" theme="fieldid" cssClass="crudForm">
	<#include "_form.ftl"/>

	<div class="formAction">
		<a href="<@s.url action="scheduleResults" searchId="${searchId!1}" currentPage="${currentPage!1}"/>"><@s.text name="label.returntoschedules"/></a>
		<@s.submit key="hbutton.save" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
	</div>
</@s.form>
