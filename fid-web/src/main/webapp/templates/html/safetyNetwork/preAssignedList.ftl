<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>

<#include '_vendorinfo.ftl'>

<div id="mainContent">
<h1><@s.text name="title.pre_assigned_assets"/></h1>

<#if page.hasResults() && page.validPage() >	
	<#assign currentAction="preAssignedAssets.action" />
	<#include '../common/_pagination.ftl' />
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
				<a href="<@s.url value="product.action" uniqueID="${product.id}" />" >${product.serialNumber}</a>
			</td>
			<#if product.rfidNumber??>
				<td>${product.rfidNumber}</td>
			<#else>
				<td></td>
			</#if>			
			<td>${product.type.name}</td>				
			<td>${product.description}</td>	
			<td><@s.text name="label.registerasset"/></td>
		</tr>
		</#list>			
	</table>
	<#include '../common/_pagination.ftl' />
</div>
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<@s.text name="label.empty_pre_assigned_asset_list" />
		</p>
	</div>
</#if>
		