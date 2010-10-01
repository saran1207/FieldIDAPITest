<div id="leftColumn">
<#if customer??>
    <a href="<@s.url value="showCustomer.action" uniqueID="${customer.id}" />" >
    	<#assign tenant = customer.tenant>
		<#include "../common/_displayTenantLogo.ftl"/>
	</a>
	<ul>
		<li><a href="<@s.url action="safetyNetwork"/>" ><@s.text name="label.safetynetworkhome"/></a></li>
		<#if action.isPublishedCatalog(customer.tenant)>
			<li><a href="<@s.url action="customerCatalog" uniqueID="${customer.tenant.id}"/>" ><@s.text name="label.view_catalog"/></a></li>
		</#if>
	</ul>
	<h3 id="companyInfo"><@s.text name="label.companyinfo"/></h3>
    <#assign org = customer/>
    <#include "../vendor/_orgAddressInfo.ftl"/>
</#if>
</div>