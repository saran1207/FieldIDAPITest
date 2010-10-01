<div id="leftColumn">
<#if vendor??>
    <a href="<@s.url value="showVendor.action" uniqueID="${vendor.id}" />" >
    	<#assign tenant = vendor.tenant>
		<#include "../common/_displayTenantLogo.ftl"/>
	</a>
	
	<ul>
		<li><a href="<@s.url action="safetyNetwork"/>" ><@s.text name="label.safetynetworkhome"/></a></li>
		<li><a href="<@s.url value="showVendor.action" uniqueID="${vendor.id}" />" > <@s.text name="label.register_asset"/></a></li>
		<li><a href="<@s.url value="preAssignedAssets.action" uniqueID="${vendor.id}" />" > <@s.text name="label.view_pre_assigned_asset"/></a></li>
		<#if action.isPublishedCatalog(vendor.tenant)>
			<li><a href="<@s.url value="vendorCatalog.action" uniqueID="${vendor.tenant.id}"/>" ><@s.text name="label.view_catalog"/></a></li>
		</#if>
	</ul>	
	<h3 id="companyInfo"><@s.text name="label.companyinfo"/></h3>
	<p>${vendor.name}</p>
    <#assign org = vendor/>
    <#include "_orgAddressInfo.ftl"/>
</#if>
</div>