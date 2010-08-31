
<div id="results">
<#if orders?exists >
	<#if !orders.isEmpty() >
		<div id="resultsTable">
			<table class="list">
				<tr>
					<th><@s.text name="${Session.sessionUser.serialNumberLabel}"/></th>
					<th><@s.text name="label.rfidnumber"/></th>
					<th><@s.text name="label.customer"/></th>
					<th><@s.text name="label.producttype"/></th>
					<th><@s.text name="label.customer_reference"/></th>
					
				</tr>
				<#list products as product >
					<tr>
						<td><a class="productLink" productId="${product.id}" href="<@s.url action="${actionTarget}" namespace="${namespace}" uniqueID="${product.id}" />" >${product.serialNumber}</a></td>
						<td>${product.rfidNumber!}</td>
						<td>${(product.owner.name)!}</td>
						<td>${product.type.name}</td>	
						<td>${(product.customerRefNumber?html)!}</td>	
					</tr>
				</#list>
			</table>
		</div>
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noresults"/></h2>
			<p>
				<@s.text name="message.emptyorderlist" />
			</p>
		</div>
	</#if>
</#if>

</div>