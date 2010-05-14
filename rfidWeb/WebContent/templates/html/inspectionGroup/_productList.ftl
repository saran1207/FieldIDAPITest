<#if !namespace?exists><#assign namespace="/"/></#if>
<div id="results">

<#if products?exists >
	<#if !products.isEmpty() >
		<h2 class="clean"><@s.text name="label.found_multiple_assets"/></h2>
		<div id="resultsTable">
			<table class="list">
				<tr>
					<th>&nbsp;</th>
					<th><@s.text name="${Session.sessionUser.serialNumberLabel}"/></th>
					<th><@s.text name="label.rfidnumber"/></th>
					<th><@s.text name="label.owner"/></th>
					<th><@s.text name="label.producttype"/></th>
					<th><@s.text name="label.identified"/></th>
					<th><@s.text name="label.reference_number"/></th>
				</tr>
				<#list products as product >
					<tr>
						<td class="selectAction"><button class="productLink" productId="${product.id}"><@s.text name="label.select"/></button></td>
						<td>${product.serialNumber?html}</td>
						<td>${(product.rfidNumber?html)!}</td>
						<td>${(product.owner.name?html)!}</td>
						<td>${product.type.name?html}</td>
						<td>${action.formatDate(product.identified, false)}</td>
						<td>${(product.customerRefNumber?html)!}</td>	
					</tr>
					
				</#list>
			</table>
		</div>
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noresults"/></h2>
			<p>
				<@s.text name="message.emptyproductlist" />
			</p>
		</div>
	</#if>
</#if>

</div>
<#if products?exists >
	<#list products as product >
		<@n4.includeScript>
			var asset = null;
			<#assign asset=product/>
			<#include "/templates/html/productCrud/_js_product.ftl"/>
			$$("[productId='${product.id}']").first().asset = asset;
		</@n4.includeScript>
	</#list>
</#if>