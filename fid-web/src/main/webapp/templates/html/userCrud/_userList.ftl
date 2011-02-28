<table class="list" id="userList">
	<tr>
		<th ><@s.text name="label.username"/></th>
    	<th ><@s.text name="label.name"/></th>
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
				<a href="<@s.url action="viewUser" uniqueID="${user.id!}" />" >${(user.userID?html)! }</a>
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
						<@s.url id="archiveUrl" action="employeeUserArchive" uniqueID="${(user.id)!}" />
					<#elseif user.liteUser>
						<@s.url id="archiveUrl" action="liteUserArchive" uniqueID="${(user.id)!}" />
					<#else>
						<@s.url  id="archiveUrl" action="readOnlyUserArchive" uniqueID="${(user.id)!}" />
					</#if>
					<div>
						<a href="${archiveUrl}" onclick="return confirm('${action.getText( 'warning.archiveuser',"", (user.userID)! )}');">
							<@s.text name="label.archive" />
						</a>
					</div>
				</#if>
			</td>
		</tr>
	</#list>
</table>