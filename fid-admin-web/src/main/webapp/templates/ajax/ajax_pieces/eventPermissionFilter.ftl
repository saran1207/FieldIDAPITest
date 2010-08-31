<table>
	<tr>
		<td>		
		 	<@s.select id="delPermission" list="usersPermissions" name="" listValue="actionName" listKey="uniqueID" label="User's Permissions"/>
		</td>
	</tr>
	<tr>
		<td>
			<input type="button" value="Remove Permission" onclick="removePermission(${permission.uniqueID});"/>
		</td>
	</tr>
</table>