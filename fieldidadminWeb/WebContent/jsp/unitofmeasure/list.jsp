<%@ taglib prefix="s" uri="/struts-tags" %>
<table>
<tr><th>Name</th><th>Type</th><th>&nbsp;</th></tr>
<s:iterator id="unitOfMeasure" value="unitOfMeasures">
	<tr><td><s:property value="name" /></td><td><s:property value="type" /></td><td><a href="unitOfMeasureCrud.action?id=<s:property value="id" />">Edit</a></tr>
</s:iterator>
</table>
<a href="unitOfMeasureCrud.action">Add New Unit Of Measure</a>