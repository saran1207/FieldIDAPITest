
${action.setPageType('job', 'edit')!}
<div class="pageSection" >
	<h2 class="decoratedHeader"><@s.text name="label.projectdetails"/></h2>
	<@s.form action="jobUpdate" theme="fieldid" cssClass="crudForm largeForm sectionContent bigForm layout">
		<@s.hidden name="uniqueID"/>
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.submit key="label.save"/>
			
			<@s.text name="label.or"/>
			<@s.url id="cancelUrl" action="job" uniqueID="${project.id}"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );"><@s.text name="label.cancel"/></a>
			
			<@s.text name="label.or"/>
			<@s.url id="deleteUrl" action="jobDelete" uniqueID="${uniqueID}"/>
			<a href="#" onclick="if( confirm( '${action.getText( 'warning.deleteproject' )}' ) ) { redirect( '${deleteUrl}' ); } return false;"><@s.text name="label.delete"/></a>
		</div>
	</@s.form>
</div>