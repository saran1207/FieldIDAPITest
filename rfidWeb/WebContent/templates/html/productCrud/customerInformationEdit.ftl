${action.setPageType('product', 'customer_edit')!}
<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>
<@s.form action="customerInformationUpdate" cssClass="crudForm" theme="fieldid">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="uniqueID"/>
	<h2>
		<#if securityGuard.jobSitesEnabled>
			<@s.text name="label.siteinformation"/>
		<#else>
			<@s.text name="label.customerinformation"/>
		</#if>
	</h2>
	
	<#if !subProduct>
		<div class="infoSet">
			<label><@s.text name="label.owner"/></label>
			<@n4.orgPicker name="owner"/>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.location"/></label>
			<@s.textfield name="location"/>
		</div>
	</#if>
	<div class="infoSet">
		<label><@s.text name="label.referencenumber"/></label>
		<@s.textfield name="customerRefNumber"/>
	</div>
	<div class="infoSet">
		<label><@s.text name="label.purchaseorder"/></label>
		<@s.textfield name="purchaseOrder"/>
	</div>
	<div class="formAction">
		<@s.submit key="label.save"/> 
		<@s.text name="label.or"/> 
		<a href="<@s.url action="product" uniqueID="${uniqueID}"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>