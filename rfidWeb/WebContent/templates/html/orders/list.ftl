<div class="ajaxWindow">

	<div>
		<@s.form action="orders" id="orderForm" theme="fieldid" cssClass="simpleInputForm">
			<label class="label"><@s.text name="label.ordernumber"/></label>
			<@s.hidden name="product"/>
			<@s.textfield name="orderNumber"/>
			<@s.submit name="load" key="hbutton.load"/>
		</@s.form>
	</div>
	
	<#if lineItems?exists >
		<#if lineItems.empty>
			<div class="emptyList" >
				<h2><@s.text name="label.noresults" /></h2>
				<p>
					<@s.text name="message.emptyorderlist" />
				</p>
			</div>
		<#else>
			<table class="list">
				<tr>
					<th class="linePcode" ><@s.text name="label.productcode"/></th>
					<th><@s.text name="label.description"/></th>
					<th class="lineQty" ><@s.text name="label.quantity"/></th>
					<th class="lineIdent" ><@s.text name="label.identifiedproducts"/></th>
			   		<th></th>
			   	</tr>
				<#list lineItems as lineItem>
					<tr>
						<td class="linePcode" >${lineItem.productCode}</td>
						<td>${lineItem.description!}</td>
						<td class="lineQty" >${lineItem.quantity}</td>
						<td class="lineIdent" >${action.getIdentifiedProductCount(lineItem)}</td>
			   			<td>
			   				<a href="javascript: void(0);" onclick="if ( confirm('<@s.text name="warning.connectorder"/>')) { selectOrder( ${product}, ${lineItem.id} ); } ">
								<@s.text name="label.select"/>
			   				</a>
			   			</td>
			   		</tr>
				</#list>
			</table>
		</#if>
	</#if>
</div>