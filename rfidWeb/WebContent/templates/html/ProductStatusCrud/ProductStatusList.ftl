
${action.setPageType('product_status', 'list')!}
<#if productStatuses.size() != 0 >
	<table class="list">
		<tr>
			<th><@s.text name="label.productstatus" /></th>
			<th></th>
		</tr>
	
		<#list productStatuses as productStatus >
		  
			<tr >
				<td>${productStatus.name}</td>
				<td>
					<a href="<@s.url value="productStatusEdit.action" uniqueID="${productStatus.uniqueID}" />" ><@s.text name="label.edit" /></a>&nbsp;&nbsp;
					<a href="<@s.url value="productStatusRemove.action" uniqueID="${productStatus.uniqueID}" />" onclick="return confirm('<@s.text name="label.areyousure" />');" ><@s.text name="label.remove" /></a>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<div class="emptyList" >
		<p>
		<@s.text name="label.emptylistproductstatuses" />
		</p>
	</div>
</#if>

