<#if customers?exists && !customers.isEmpty()>
	<table class="list" id="customerTable">
		<tr>
			<th></th>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.id" /></th>
			<th><@s.text name="label.organization" /></th>
			<th><@s.text name="label.created" /></th>
		</tr>
	
	<#list customers as customer>
		<tr>
			<td><button onclick="selectWinner('${customer.id}', '${customer.name}', '${customer.code!}', '${action.formatDateTime(customer.created)}' );"> <@s.text name="label.select"/></button></td>
			<td>${customer.name}</td>
			<td>${customer.code!}</td>
			<td>${customer.getInternalOrg().name}</td>
			<td>${action.formatDateTime(customer.created)}</td>
		</tr>
		<#if customer_index+1 == 10>
			<#assign tooManyResults=true>	
			<#break>
		</#if>
	</#list>
	</table>
	<#if tooManyResults?exists && tooManyResults>
		<@s.text name="message.customer_merge_search_refine_results" >
			<@s.param>${customers.size()}</@s.param>
		</@s.text>
	</#if>
<#else>
No results
</#if>