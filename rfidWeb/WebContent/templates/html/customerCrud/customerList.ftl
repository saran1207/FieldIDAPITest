${action.setPageType('customer','list')!}


<div class="listFilter quickForm" >
	<@s.form action="customerList" method="get">
		<@s.textfield key="label.filtername" name="listFilter" id="listFilter" labelposition="left" />
		<div class="formAction">
			<@s.submit key="hbutton.filter" />
		</div>
	</@s.form>
</div>


<#if  page.hasResults() && page.validPage() >
	
	<#assign currentAction="customerList.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.customer" /></th>
			<th></th>
		</tr>
	
	<#list page.list as customer>
	
		<tr >
			<td><a href="<@s.url value="customerShow.action" uniqueID="${customer.id}" />" >${customer.name} - (${customer.customerId})</a></td>
			<td>
				<a href="<@s.url value="customerEdit.action" uniqueID="${customer.id}" />" ><@s.text name="label.edit" /></a> | 
				<a href="<@s.url value="customerRemove.action" uniqueID="${customer.id}" />" onclick="return confirm('<@s.text name="label.areyousure" />');" ><@s.text name="label.remove" /></a>
			</td>
		</tr>
	</#list>
	</table>
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
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


