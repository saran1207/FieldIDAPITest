${action.setPageType('event_type_group', 'edit')!}
<div class="pageSection" >
	<h2><@s.text name="label.groupdetails"/></h2>
	<@s.form action="eventTypeGroupUpdate" theme="fieldid" cssClass="crudForm sectionContent bigForm">
		<@s.hidden name="uniqueID"/>
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.url id="deleteUrl" action="eventTypeGroupDelete" uniqueID="${uniqueID}"/>
			<@s.reset name="delete" key="label.delete"  onclick="if( confirm( '${action.getText( 'warning.deleteeventtypegroup' )}' ) ) { redirect( '${deleteUrl}' ); } return false;"/>
			<@s.url id="cancelUrl" action="eventTypeGroup" uniqueID="${uniqueID}"/>
			<@s.reset key="label.cancel" onclick="return redirect( '${cancelUrl}' );"/>
			<@s.submit key="label.save"/>
		</div>
	</@s.form>
</div>
<script type="text/javascript">
	<#if !action.canBeDeleted(group)>
		$$("input[name='delete']").first().disable();
		
	</#if>
</script>
