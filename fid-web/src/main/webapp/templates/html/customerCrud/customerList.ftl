<head>
	<@n4.includeStyle href="customerList" type="page"/>
</head>

<#if page.hasResults() && page.validPage() >
	
	<#include "_listFilter.ftl">

	<#assign currentAction="customerList.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list" id="customerTable">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.id" /></th>
			<th><@s.text name="label.organization" /></th>
			<th><@s.text name="label.created" /></th>
			<th><@s.text name="label.last_modified" /></th>					
			<th></th>
		</tr>
	
	<#list page.list as customer>

		<tr>
			<td class="notranslate">
				<#if customer.archived>
					${customer.name}
				<#else>
					<a href="<@s.url value="customerShow.action" uniqueID="${customer.id}" />" >${customer.name}</a>
				</#if>
			</td>
			<td class="notranslate">${customer.code!}</td>
			<td class="notranslate">${customer.getInternalOrg().name}</td>
			<td class="notranslate"><#if customer.createdBy?exists>${customer.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(customer.created)}</td>
			<td class="notranslate"><#if customer.modifiedBy?exists>${customer.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(customer.modified)}</td>
			<td>
				<#if customer.linked >
				<a href="<@s.url value="customerEdit.action" uniqueID="${customer.id}" />" ><@s.text name="label.edit" /></a> | 
					<@s.text name="label.linked_customer" />
				<#elseif customer.archived>
					<a href="<@s.url value="customerUnarchive.action" uniqueID="${customer.id}" />" ><@s.text name="label.unarchive" /></a>
				<#else>
					<a href="<@s.url value="customerEdit.action" uniqueID="${customer.id}" />" ><@s.text name="label.edit" /></a> |
					<a href="<@s.url value="customerArchive.action" uniqueID="${customer.id}" />" onclick="${action.getAppropriateJSValue(customer.id)}" ><@s.text name="label.archive" /></a>
				</#if>
			</td>
		</tr>
	</#list>
	</table>
	<#include '../common/_pagination.ftl' />
	<div class="total"><@s.text name="label.total"/>:&nbsp;${page.totalResults}</div>
<#elseif !page.hasResults() && !nameFilter??>
	<@s.url id="addCustomerUrl" action="customerEdit"/>
	
	<div class="initialMessage">
		<div class="textContainer" >
			<h1><@s.text name="label.create_owner"/></h1>
			<p><@s.text name="label.create_owner_message" /></p>
		</div>
			<input type="submit" value="<@s.text name="label.create_owner_now"/>"onclick="return redirect('${addCustomerUrl}');"/>
	</div>
<#elseif !page.hasResults() && nameFilter??>
	
	<#include "_listFilter.ftl">	
	
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<@s.text name="label.emptylistcustomers" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage"/></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="customerList" currentPage="1"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>


