<#if !lineItems.isEmpty() >
	<div id="resultsTable">
		<h2 class="orderTitle" ><@s.text name="label.lineitems"/></h2>
		<table class="list">
			<tr>
				<th class="lineAddLink" ></th>
				<th class="linePcode" ><@s.text name="label.assetcode"/></th>
				<th><@s.text name="label.description"/></th>
				<th class="lineQty" ><@s.text name="label.quantity"/></th>
				<th class="lineIdent" ><@s.text name="label.identifiedassets"/></th>
			</tr>
			<#list lineItems as lineItem >
				<tr>
					<td class="lineAddLink" >
						<a href="<@s.url action="assetAddWithOrder" lineItemId="${lineItem.id}" tagOptionId="${tagOptionId}" />"><@s.text name="label.identifysingle" /></a>
						|
						<a href="<@s.url action="assetMultiAddWithOrder" lineItemId="${lineItem.id}" tagOptionId="${tagOptionId}" />"><@s.text name="label.identifymultiple" /></a>
					</td>
					<td class="linePcode" >${lineItem.assetCode}</td>
					<td>${lineItem.description!}</td>
					<td class="lineQty" >${lineItem.quantity}</td>
					<td class="lineIdent" >${action.getIdentifiedAssetCount(lineItem)}</td>
				</tr>
			</#list>
		</table>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<@s.text name="message.emptylineitemlist" />
		</p>
	</div>
</#if>