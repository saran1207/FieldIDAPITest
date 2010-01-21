<#assign html>
<table class="list">
	<tr>
		<th></th>
		<th><@s.text name="label.serialnumber"/></th>
		<th><@s.text name="label.rfidnumber"/></th>
		<th><@s.text name="label.reference_number"/></th>
		<th><@s.text name="label.vendor"/></th>
		<th><@s.text name="label.producttype"/></th>
	</tr>
	<#list products as product>
		<tr>
			<td>
				<button class="selectProduct" name="select_${product.id}" productId="${product.id}" 
					serialNumber="${product.serialNumber?default('')?html}"
					rfidNumber="${product.rfidNumber?default('')?html}"
					owner="${product.owner.internalOrg.name?html}"
					productType="${product.type.name?html}"
					referenceNumber="${product.customerRefNumber?default('')?html}"
					><@s.text name="label.select"/></button>
			</td>
			<td>${product.serialNumber?html}</td>
			<td>${(product.rfidNumber?html)!} </td>
			<td>${(product.customerRefNumber?html)!} </td>
			<td>${product.owner.internalOrg.name?html}</td>
			<td>${product.type.name?html}</td>
		</tr>
	</#list>
</table>
</#assign>

$('snSmartSearchResults').update('${html?js_string}');
$$(".selectProduct").each(function (element) { element.observe("click", updateLinkedProductFromMultipleResults); } );