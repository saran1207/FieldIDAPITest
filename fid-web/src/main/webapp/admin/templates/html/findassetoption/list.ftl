
<table>
<tr>
	<th>Tenant Name</th>
	<th>Description</th>
	<th>Value</th>
	<th>Key</th>
	<th>Weight</th>
	<th>Mobile Weight</th>	
</tr>
<@s.iterator id="findAssetOption" value="findAssetOptions">
	<tr>
	<td><@s.property value="tenant.displayName" /></td>
	<td><@s.property value="findAssetOption.description" /></td>
	<td><@s.property value="findAssetOption.value" /></td>
	<td><@s.property value="findAssetOption.key" /></td>
	<td><@s.property value="weight" /></td>
	<td><@s.property value="mobileWeight" /></td>	
	<td><a href="findAssetOptionCrud.action?id=<@s.property value="uniqueID" />">Edit</a></tr>
</@s.iterator>
</table>