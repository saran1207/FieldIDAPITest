<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="screenCrud!save" method="post">
	<s:hidden name="id" value="%{id}" />
	<s:textfield name="screen.description" value="%{screen.description}" label="Description" />
	<s:submit />
</s:form>
<h3>Button Groups:</h3>
<table>
<tr><th>Name</th><th>Buttons</th></tr>
<s:iterator id="buttonGroup" value="screen.buttonGroups">
<tr>
	<td><s:property value="name" /></td>
	<td>
		<table>
		<tr><th>Name</th><th>Value</th></tr>
		<s:iterator id="button" value="buttonGroup.buttons">
		<tr><td><s:property value="name" /></td><td><s:property value="value" /></td></tr>
		</s:iterator>
		</table>
	</td>
</tr>
</s:iterator>
</table>