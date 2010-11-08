${action.setPageType('asset_type_group', 'edit')!}
<div class="pageSection" >
	<h2 class="decoratedHeader"><@s.text name="label.groupdetails"/></h2>

	<@s.form action="assetTypeGroupUpdate" theme="fieldid" cssClass="crudForm bigForm pageSection layout">
		<@s.hidden name="uniqueID"/>
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.url id="cancelUrl" action="assetTypeGroup" uniqueID="${uniqueID}"/>
			<@s.reset key="label.cancel" onclick="return redirect( '${cancelUrl}' );" />
			<@s.submit key="label.save"/>
		</div>
	</@s.form>
</div>