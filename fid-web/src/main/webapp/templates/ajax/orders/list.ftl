
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
				<#list assets as asset >
					<tr>
						<td><a class="productLink" assetId="${asset.id}" href="<@s.url action="${actionTarget}" namespace="${namespace}" uniqueID="${asset.id}" />" >${asset.serialNumber}</a></td>
						<td>${asset.rfidNumber!}</td>
						<td>${(asset.owner.name)!}</td>
						<td>${asset.type.name}</td>
						<td>${(asset.customerRefNumber?html)!}</td>
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