${action.setPageType('event_type_group', 'add')!}
<div class="pageSection" >

	<@s.form action="eventTypeGroupCreate" theme="fieldid" cssClass="crudForm bigForm pageSection layout">
		
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.url id="cancelUrl" action="eventTypeGroups"/>
			<@s.submit key="label.save"/>
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</div>