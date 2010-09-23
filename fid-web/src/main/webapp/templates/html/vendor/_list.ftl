<#if page.hasResults() && page.validPage() >	
	<#assign currentAction="preAssignedAssets.action" />
	<#include '../common/_pagination.ftl' />
    <#include "../common/_lightView.ftl" />
    <#include '_lightViewBoxOptions.ftl'>
	<table class="list" id="productTable">
		<tr>
			<th><@s.text name="label.serialnumber" /></th>
			<th><@s.text name="label.rfidnumber" /></th>
			<th><@s.text name="label.producttype" /></th>
			<th><@s.text name="label.desc" /></th>
			<th></th>
		</tr>
		
		<#list page.list as product>
		<tr>
			<td>
				<a href='<@s.url value="showNetworkProduct.action" uniqueID="${product.id}" useContext="true"/>' >${product.serialNumber}</a>
			</td>
			<#if product.rfidNumber??>
				<td>${product.rfidNumber}</td>
			<#else>
				<td></td>
			</#if>			
			<td>${product.type.name}</td>				
			<td>${product.description}</td>	
			<td>
			
				<a href='<@s.url action="registerProduct.action" namespace="/aHtml/iframe" uniqueID="${product.id}"/>' ${lightViewOptions} ><@s.text name="label.registerasset"/></a>
				
			</td>
		</tr>
		</#list>			
	</table>
	<#include '../common/_pagination.ftl' />
</div>
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<#if isSearch>
				<@s.text name="label.empty_network_asset" />				
			<#else>
				<@s.text name="label.empty_pre_assigned_asset_list" />
			</#if>
		</p>
	</div>
</#if>