
${action.setPageType('job', 'edit')!}
<div class="pageSection" >
	<h2 class="decoratedHeader"><@s.text name="label.projectdetails"/></h2>
	<@s.form action="jobUpdate" theme="fieldid" cssClass="crudForm largeForm sectionContent bigForm layout">
		<@s.hidden name="uniqueID"/>
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.url id="deleteUrl" action="jobDelete" uniqueID="${uniqueID}"/>
			<@s.reset key="label.delete" onclick="if( confirm( '${action.getText( 'warning.deleteproject' )}' ) ) { redirect( '${deleteUrl}' ); } return false;"/>
			<@s.url id="cancelUrl" action="job" uniqueID="${project.id}"/>
			<@s.reset key="label.cancel" onclick="return redirect( '${cancelUrl}' );"/>
			<@s.submit key="label.save"/>
		</div>
	</@s.form>
</div>