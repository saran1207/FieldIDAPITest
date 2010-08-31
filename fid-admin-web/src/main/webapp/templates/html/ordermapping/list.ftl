
<table>
<tr><th>Organization ID</th><th>External Source ID</th><th>Order Key</th><th>Source Order Key</th><th>&nbsp;</th><th>&nbsp;</th></tr>
<@s.iterator id="orderMapping" value="orderMappings">
	<tr>
		<td><@s.property value="organizationID" /></td>
		<td><@s.property value="externalSourceID" /></td>
		<td><@s.property value="orderKey" /></td>
		<td><@s.property value="sourceOrderKey" /></td>
		<td><a href="orderMappingCrud.action?id=<@s.property value="uniqueID" />">Edit</a></td>
		<td><a href="orderMappingDelete.action?id=<@s.property value="uniqueID" />" onclick=="return confim('Are you sure you wish to delete this?');" >Delete</a></td>
	</tr>
</@s.iterator>
</table>
<a href="orderMappingCrud.action">Add New Order Mapping</a>
<br /><br /><br />
<@s.form action="uploadOrderMappingXml!xmlUpload" method="post" enctype="multipart/form-data">
	<@s.file name="orderMappingXml"  label="Upload Order Mapping XML" />
	<@s.submit />
</@s.form>