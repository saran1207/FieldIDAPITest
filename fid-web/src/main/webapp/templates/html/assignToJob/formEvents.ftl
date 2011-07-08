<title><@s.text name="title.assigntojob" /></title>

<@s.form action="assignEventsToJob" theme="fieldid" cssClass="crudForm">
	<#include "_form.ftl"/>
	<div class="formAction">
		<a href="/fieldid/w/returnToReport"><@s.text name="label.returntoreport"/></a>
		<@s.submit key="hbutton.save" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
	</div>
</@s.form>
