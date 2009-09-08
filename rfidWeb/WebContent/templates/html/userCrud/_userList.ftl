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
		<tr id="user_${user.uniqueID!}" >
			<td><a href="<@s.url action="userEdit" uniqueID="${user.uniqueID!}" />" >${user.userID?html! }</a> </td>
			<td>${user.userLabel?html! }</td>
			<td>${(user.owner.getInternalOrg().name?html)!}</td>
			<#if userType?exists && userType != "EMPLOYEES" >
				<td>
					${(user.owner.customerOrg.name?html)!}
				</td>
				<td>
					${(user.owner.divisionOrg.name?html)!}
				</td>
			</#if>
			<td>${user.emailAddress?html! } </td>
			<td>
				<#if user.uniqueID != Session.sessionUser.uniqueID && !user.admin >
					<div><a href="javascript:void(0);" onclick="deleteUser( '<@s.url action="userDelete" namespace="/ajax" uniqueID="${(user.uniqueID)!}" />', '${action.getText( 'warning.deleteuser',"", user.userID )}' ); return false;" ><@s.text name="label.remove" /></a></div>
				</#if>
			</td>
		</tr>
	</#list>
</table>

<script type="text/javascript" >
	
	function deleteUser( url, message ) {
		if( confirm( message ) ) {
			new Ajax.Request( url, { 
				method: "get", 
				onComplete: function( transport ) { 
					deleteUserCallback( transport.responseText ); 
				} 
			} );
		}	
	}
	
	function deleteUserCallback( response ) {
		var jsonObject = JSON.parse( response );
		
		if( jsonObject.status == 0 ) {
			if( jsonObject.uniqueID != null ) {
				var userRow = $( 'user_' + jsonObject.uniqueID );
				if( userRow != null ) {
					userRow.parentNode.removeChild( userRow );
				}
			}
		}
		
		updateMessages( jsonObject.messages, jsonObject.errors );
	}
	

	

</script>