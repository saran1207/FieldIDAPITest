<div id="leftColumn">
<#if vendor??>
    <a href="<@s.url value="showVendor.action" uniqueID="${vendor.id}" />" >
    	<#assign tenant = vendor.tenant>
		<#include "../common/_displayTenantLogo.ftl"/>
	</a>
	
	<ul>
		<li><a href="<@s.url value="showVendor.action" uniqueID="${vendor.id}" />" > <@s.text name="label.register_asset"/></a></li>
		<li><a href="<@s.url value="preAssignedAssets.action" uniqueID="${vendor.id}" />" > <@s.text name="label.view_pre_assigned_asset"/></a></li>
		<li><a href="<@s.url value="publishedCatalog.action" uniqueID="${vendor.tenant.id}"/>" ><@s.text name="label.view_catalog"/></a></li>
	</ul>	
	<h3 id="companyInfo"><@s.text name="label.companyinfo"/></h3>
	<p>${vendor.name}</p>
    <#assign org = vendor/>
    <#include "_orgAddressInfo.ftl"/>
</#if>
</div>