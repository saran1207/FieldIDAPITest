${action.setPageType('event_type_group', 'add')!}
<div class="pageSection" >
	<h2><@s.text name="label.groupdetails"/></h2>

	<@s.form action="eventTypeGroupCreate" theme="fieldid" cssClass="crudForm bigForm pageSection layout">
		
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.url id="cancelUrl" action="eventTypeGroups"/>
			<@s.reset key="label.cancel" onclick="return redirect( '${cancelUrl}' );" />
			<@s.submit key="label.save"/>
		</div>
	</@s.form>
</div>