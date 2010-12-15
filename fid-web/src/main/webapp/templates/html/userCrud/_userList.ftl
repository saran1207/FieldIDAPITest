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
		<th><@s.text name="label.lastlogin"/></th>
		<th></th>
	<tr>
	<#assign count=0 >
	<#list userList as user >
		<tr id="user_${user.id!}" >
			<td>
				<a href="<@s.url action="viewUser" uniqueID="${user.id!}" />" >${user.userID?html! }</a>
			</td>
			<td>${user.userLabel?html! }</td>
			<td>${(user.owner.getInternalOrg().name?html)!}</td>
			<#if userType?exists && userType != "EMPLOYEES" >
				<td>${(user.owner.customerOrg.name?html)!}&nbsp;</td>
				<td>${(user.owner.divisionOrg.name?html)!}&nbsp;</td>
			</#if>
			
			<td>${user.emailAddress?html! } </td>
			
			<td>${(action.dateCreated(user)??)?string(action.formatDateTime(action.dateCreated(user)), "--")}</td>		
			<td>
				<#if user.id != sessionUser.id && !user.admin >
					<#if user.fullUser>
						<@s.url id="deleteUrl" namespace="/ajax" action="employeeUserDelete" uniqueID="${(user.id)!}" />
					<#elseif user.liteUser>
						<@s.url id="deleteUrl" namespace="/ajax" action="liteUserDelete" uniqueID="${(user.id)!}" />
					<#else>
						<@s.url  id="deleteUrl" namespace="/ajax" action="readOnlyUserDelete" uniqueID="${(user.id)!}" />
					</#if>
					<div><a href="#deleteUser" onclick="deleteUser('${deleteUrl}', '${action.getText( 'warning.deleteuser',"", user.userID )}' ); return false;" ><@s.text name="label.remove" /></a></div>
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