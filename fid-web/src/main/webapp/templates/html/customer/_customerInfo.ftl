<div id="leftColumn">
<#if customer??>
    <a href="<@s.url value="showCustomer.action" uniqueID="${customer.id}" />" >
    	<#assign tenant = customer>
		<#include "../common/_displayTenantLogo.ftl"/>
	</a>
	<ul>
		<li><a href="<@s.url action="publishedCatalog" uniqueID="${customer.id}"/>" ><@s.text name="label.view_catalog"/></a></li>
	</ul>
	<h3 id="companyInfo"><@s.text name="label.companyinfo"/></h3>
	<p>${customer.name}</p>
	<p>${customer.addressInfo.streetAddress}</p>
	<p>${customer.addressInfo.city}, ${customer.addressInfo.state}, ${customer.addressInfo.country} </p>
	<p>${customer.addressInfo.zip}</p>
	<p><@s.text name="label.phone"/>: ${customer.addressInfo.phone1}</p>
	<p><@s.text name="label.fax"/>: ${customer.addressInfo.fax1}</p>
	<ul>
		<#if customer.webSite??>
			<li><a href="<@s.url value="${customer.webSite}"/>" target="_blank">${customer.webSite}</a></li>
		</#if>
	</ul>
</#if>
</div>