<table class="list" id="userList">
	<tr>
		<th ><@s.text name="label.userid"/></th>
    	<th ><@s.text name="label.username"/></th>
    	<th ><@s.text name="label.organization"/></th>
    	<#if userType?exists && userType != "EMPLOYEES"  >
    		<th ><@s.text name="label.customer"/></th>
    		<th ><@s.text name="label.division"/></th>
    	</#if>
  		<th ><@s.text name="label.emailaddress"/></th>
		<th></th>
	<tr>
	<#assign count=0 >
	<#list userList as user >
		<tr id="user_${user.id!}" >
			<td><a href="<@s.url action="${user.employee?string('employeeUserEdit', 'customerUserEdit')}" uniqueID="${user.id!}" />" >${user.userID?html! }</a> </td>
			<td>${user.userLabel?html! }</td>
			<td>${(user.owner.getInternalOrg().name?html)!}</td>
			<#if userType?exists && userType != "EMPLOYEES" >
				<td>${(user.owner.customerOrg.name?html)!}</td>
				<td>${(user.owner.divisionOrg.name?html)!}</td>
			</#if>
			<td>${user.emailAddress?html! } </td>
			<td>
				<#if user.uniqueID != Session.sessionUser.uniqueID && !user.admin >
					<div><a href="#deleteUser" onclick="deleteUser('<@s.url action="${user.employee?string('employeeUserDelete', 'customerUserDelete')}" namespace="/ajax" uniqueID="${(user.uniqueID)!}" />', '${action.getText( 'warning.deleteuser',"", user.userID )}' ); return false;" ><@s.text name="label.remove" /></a></div>
				</#if>
			</td>
		</tr>
	</#list>
</table>

<script type="text/javascript" >
	
	function deleteUser(url, message) {
		if (confirm(message)) {
			getResponse(url); 
		}	
	}
	
	function deleteUserCallback(jsonObject) {
		
		if(jsonObject.status == 0) {
			if(jsonObject.uniqueID != null) {
				var userRow = $('user_' + jsonObject.uniqueID);
				if(userRow != null) {
					userRow.parentNode.removeChild(userRow);
				}
			}
		}
		
		updateMessages(jsonObject.messages, jsonObject.errors);
	}
	

	

</script>