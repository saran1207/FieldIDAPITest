${action.setPageType('product', 'traceability')!}
<#list linkedProducts as product>
	<#assign tenant=product.tenant/>
	<#include "../common/_displayTenantLogo.ftl"/>${product.owner.internalOrg.name} 
	<div class="viewSection smallViewSection" >
		<h2><@s.text name="label.productsummary"/></h2>
		<p>
			
			<label><@s.text name="${sessionUser.serialNumberLabel}"/></label>
			<span class="fieldValue">${product.serialNumber}</span>
		</p>
		<p>
			<label><@s.text name="label.rfidnumber"/></label>
			<span class="fieldValue">${product.rfidNumber!}</span>
		</p>
		<p>
			<label><@s.text name="label.producttype"/></label>
			<span class="fieldValue">${product.type.name}</span>
		</p>
		<p>
			<label><@s.text name="label.productstatus"/></label>
			<span class="fieldValue">${(product.productStatus.name)!}</span>
		</p>
		<p>
			<label><@s.text name="label.identified"/></label>
			<span class="fieldValue">${action.formatDate(product.identified, false)}</span>
		</p>
		<#if !securityGuard.integrationEnabled >
		<p>
			<label><@s.text name="label.ordernumber"/></label>
			<span class="fieldValue">${(product.shopOrder.order.orderNumber)!}</span>
		</p>
		</#if>
	</div>
	<#include "_customerInformation.ftl" />
	<#if !product.orderedInfoOptionList.isEmpty() >
		<div class="viewSection smallViewSection" >
			<h2>${product.type.name} <@s.text name="label.attributes"/></h2>
			<#list product.orderedInfoOptionList as infoOption >
				<p>
					<label>${infoOption.infoField.name} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </label>
					<span class="fieldValue">${infoOption.name}</span>
				</p>
			</#list>
		</div>
	</#if>
	
	<#if product.type.warnings?exists && product.type.warnings?length gt 0 >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.warnings"/></h2>
			<p class="fieldValue">
				${product.type.warnings!}
			</p>
		</div>
	</#if>
	
	<#if product.type.instructions?exists && product.type.instructions?length gt 0 >
		<div class="viewSection smallViewSection" >
			<h2><@s.text name="label.instructions"/></h2>
			<p class="fieldValue">
				${product.type.instructions!}
			</p>
		</div>
	</#if>
		
</#list>

