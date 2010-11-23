${action.setPageType('event_type_group', 'edit')!}
<div class="pageSection" >
	<@s.form action="eventTypeGroupUpdate" theme="fieldid" cssClass="crudForm sectionContent bigForm">
		<@s.hidden name="uniqueID"/>
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.url id="deleteUrl" action="eventTypeGroupDelete" uniqueID="${uniqueID}"/>
			<@s.url id="cancelUrl" action="eventTypeGroup" uniqueID="${uniqueID}"/>
			<@s.submit key="label.save"/>
			&nbsp;<@s.text name="label.or"/>
			<a href="javascript:void(0);" onclick="return redirect('${cancelUrl}'); return false;"/><@s.text name="label.cancel"/></a>
			<#if action.canBeDeleted(group)>
				&nbsp;<@s.text name="label.or"/>&nbsp;
				<a href="#" onclick="if( confirm( '${action.getText( 'warning.deleteeventtypegroup' )}' ) ) { redirect( '${deleteUrl}' ); } return false;" ><@s.text name="label.delete"/></a>
			</#if>
		</div>
	</@s.form>
</div>
