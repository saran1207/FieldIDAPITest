<head>
	<@n4.includeStyle href="customerList" type="page"/>
</head>



<#if page.hasResults() && page.validPage() >
	<div class="listFilter quickForm" >
		<@s.form action="${filterAction}" method="get">
			<@s.textfield key="label.filtername" name="listFilter" id="listFilter" labelposition="left" />
			<div class="formAction">
				<@s.submit key="hbutton.filter" />
			</div>
		</@s.form>
	</div>

	<#assign currentAction="customerList.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list" id="customerTable">
		<tr>
			<th><@s.text name="label.customername" /></th>
			<th><@s.text name="label.customerid" /></th>
			<th><@s.text name="label.organization" /></th>
			<th><@s.text name="label.created" /></th>
			<th><@s.text name="label.last_modified" /></th>					
			<th></th>
		</tr>
	
	<#list page.list as customer>
	
		<tr>
			<td>
				<#if customer.archived>
					${customer.name}
				<#else>
					<a href="<@s.url value="customerShow.action" uniqueID="${customer.id}" />" >${customer.name}</a>
				</#if>
			</td>
			<td>${customer.code!}</td>
			<td>${customer.getInternalOrg().name}</td>
			<td><#if customer.createdBy?exists>${customer.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(customer.created)}</td>
			<td><#if customer.modifiedBy?exists>${customer.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(customer.modified)}</td>
			<td>
				<#if customer.linked >
					<@s.text name="label.linked_customer" />
				<#elseif customer.archived>
					<a href="<@s.url value="customerUnarchive.action" uniqueID="${customer.id}" />" ><@s.text name="label.unarchive" /></a>
				<#else>
					<a href="<@s.url value="customerEdit.action" uniqueID="${customer.id}" />" ><@s.text name="label.edit" /></a> | 
					<a href="<@s.url value="customerArchive.action" uniqueID="${customer.id}" />" onclick="return confirm('<@s.text name="label.areyousurearchivecustomer" />');" ><@s.text name="label.archive" /></a>
				</#if>
			</td>
		</tr>
	</#list>
	</table>
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() && !listFilter??>
	<@s.url id="addCustomerUrl" action="customerEdit"/>
	
	<div class="initialMessage">
		<div class="textContainer" >
			<h1><@s.text name="label.create_owner"/></h1>
			<p><@s.text name="label.create_owner_message" /></p>
		</div>
			<input type="submit" value="<@s.text name="label.create_owner_now"/>"onclick="return redirect('${addCustomerUrl}');"/>
	</div>
<#elseif !page.hasResults() && listFilter??>
	<div class="listFilter quickForm" >
		<@s.form action="${filterAction}" method="get">
			<@s.textfield key="label.filtername" name="listFilter" id="listFilter" labelposition="left" />
			<div class="formAction">
				<@s.submit key="hbutton.filter" />
			</div>
		</@s.form>
	</div>
	
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


