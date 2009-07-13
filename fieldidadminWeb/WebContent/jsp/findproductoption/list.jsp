<%@ taglib prefix="s" uri="/struts-tags" %>
<table>
<tr>
	<th>Tenant Name</th>
	<th>Description</th>
	<th>Value</th>
	<th>Key</th>
	<th>Weight</th>
	<th>Mobile Weight</th>	
</tr>
<s:iterator id="findProductOption" value="findProductOptions">
	<tr>
	<td><s:property value="tenant.displayName" /></td>
	<td><s:property value="findProductOption.description" /></td>
	<td><s:property value="findProductOption.value" /></td>
	<td><s:property value="findProductOption.key" /></td>
	<td><s:property value="weight" /></td>
	<td><s:property value="mobileWeight" /></td>	
	<td><a href="findProductOptionCrud.action?id=<s:property value="uniqueID" />">Edit</a></tr>
</s:iterator>
</table>