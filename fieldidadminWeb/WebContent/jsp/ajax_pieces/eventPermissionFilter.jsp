<%@ taglib prefix="s" uri="/struts-tags" %>
<table>
	<tr>
		<td>		
		 	<s:select id="delPermission" list="usersPermissions" name="" listValue="actionName" listKey="uniqueID" label="User's Permissions"/>
		</td>
	</tr>
	<tr>
		<td>
			<input type="button" value="Remove Permission" onclick="removePermission(<s:property value="permission.uniqueID"/>);"/>
		</td>
	</tr>
</table>