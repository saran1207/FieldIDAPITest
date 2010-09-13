<div id="leftColumn">
<#if vendor??>
    <a href="<@s.url value="showVendor.action" uniqueID="${vendor.id}" />" >
    	<#assign tenant = vendor>
		<#include "../common/_displayTenantLogo.ftl"/>
	</a>
	
	<ul>
		<li><a href="<@s.url value="showVendor.action" uniqueID="${vendor.id}" />" > <@s.text name="label.register_asset"/></a></li>
		<li><a href="<@s.url value="preAssignedAssets.action" uniqueID="${vendor.id}" />" > <@s.text name="label.view_pre_assigned_asset"/></a></li>
		<li><a href="<@s.url value="publishedCatalog.action" uniqueID="${vendor.id}"/>" ><@s.text name="label.view_catalog"/></a></li>
	</ul>	
	<h3 id="companyInfo"><@s.text name="label.companyinfo"/></h3>
	<p>${vendor.name}</p>
	<p>${vendor.addressInfo.streetAddress}</p>				
	<p>${vendor.addressInfo.city}, ${vendor.addressInfo.state}, ${vendor.addressInfo.country} </p>				
	<p>${vendor.addressInfo.zip}</p>
	<p><@s.text name="label.phone"/>: ${vendor.addressInfo.phone1}</p>
	<p><@s.text name="label.fax"/>: ${vendor.addressInfo.fax1}</p>		
	<ul>
		<#if vendor.webSite??>
			<li><a href="<@s.url value="${vendor.webSite}"/>" target="_blank">${vendor.webSite}</a></li>
		</#if>
	</ul>		
</#if>
</div>