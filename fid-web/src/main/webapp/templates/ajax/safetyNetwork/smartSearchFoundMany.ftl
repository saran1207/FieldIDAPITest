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
	<#list assets as asset>
		<tr>
			<td>
				<button class="selectProduct" name="select_${asset.id}" assetId="${asset.id}"
					serialNumber="${asset.serialNumber?default('')?html}"
					rfidNumber="${asset.rfidNumber?default('')?html}"
					owner="${asset.owner.internalOrg.name?html}"
					assetType="${asset.type.name?html}"
					referenceNumber="${asset.customerRefNumber?default('')?html}"
					><@s.text name="label.select"/></button>
			</td>
			<td>${asset.serialNumber?html}</td>
			<td>${(asset.rfidNumber?html)!} </td>
			<td>${(asset.customerRefNumber?html)!} </td>
			<td>${asset.owner.internalOrg.name?html}</td>
			<td>${asset.type.name?html}</td>
		</tr>
	</#list>
</table>
</#assign>

$('snSmartSearchResults').update('${html?js_string}');
$$(".selectProduct").each(function (element) { element.observe("click", updateLinkedProductFromMultipleResults); } );