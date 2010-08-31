${action.setPageType('product_type_group', 'edit')!}
<div class="pageSection" >
	<h2><@s.text name="label.groupdetails"/></h2>

	<@s.form action="productTypeGroupUpdate" theme="fieldid" cssClass="crudForm bigForm pageSection layout">
		<@s.hidden name="uniqueID"/>
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.url id="cancelUrl" action="productTypeGroup" uniqueID="${uniqueID}"/>
			<@s.reset key="label.cancel" onclick="return redirect( '${cancelUrl}' );" />
			<@s.submit key="label.save"/>
		</div>
	</@s.form>
</div>