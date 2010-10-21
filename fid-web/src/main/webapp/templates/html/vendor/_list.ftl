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
		
		<#list page.list as asset>
		<tr>
			<td>
				<a href='<@s.url value="showNetworkProduct.action" uniqueID="${asset.id}"/>' >${asset.serialNumber}</a>
			</td>
			<#if asset.rfidNumber??>
				<td>${asset.rfidNumber}</td>
			<#else>
				<td></td>
			</#if>			
			<td>${asset.type.name}</td>
			<td>${asset.description}</td>
			<td>
			    <#if action.isAssetAlreadyRegistered(asset)>
                    <@s.text name="label.already_registered"/>
                <#else>
                    <a href='<@s.url action="regNetworkProduct.action" namespace="/aHtml/iframe" uniqueID="${asset.id}"/>' ${lightViewOptions} ><@s.text name="label.registerasset"/></a>
                </#if>

				
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
<#else>
	<#if isSearch>
		<@s.url value="searchNetworkProduct.action" uniqueID="${uniqueID}" id="url"/>				
	<#else>
		<@s.url value="preAssignedAssets.action" uniqueID="${uniqueID}" id="url"/>				
	</#if>
	<script type="text/javascript">
		var lastPage = ${page.lastPage} + 1
		window.location.href = '${url}' + '\&currentPage=' + lastPage;
	</script>
</#if>