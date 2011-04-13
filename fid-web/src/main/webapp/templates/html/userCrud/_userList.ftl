<table class="list" id="userList">
	<tr>
		<@s.text id="usernamelabel" name="label.username"/>
    	<@s.text id="namelabel" name="label.name_first_last"/>
    	<@s.text id="orglabel" name="label.organization"/>
		<@s.text id="customerlabel" name="label.customer"/>
		<@s.text id="divisionlabel" name="label.division"/>
  		<@s.text id="emaillabel" name="label.emailaddress"/>

		<#if !isArchivedPage>
			<#assign columns = ["userID", "", "owner", "owner.customerOrg", "owner.divisionOrg", "emailAddress"]>
			<#assign labels = ["${usernamelabel}", "${namelabel}", "${orglabel}", "${customerlabel}", "${divisionlabel}", "${emaillabel}"]>
			<#assign sortAction = "userList" >
		<#else>
			<#assign columns = ["","owner", "owner.customerOrg", "owner.divisionOrg", "emailAddress"]>
			<#assign labels = ["${namelabel}", "${orglabel}", "${customerlabel}", "${divisionlabel}", "${emaillabel}"]>
			<#assign sortAction = "archivedUserList" >			
		</#if>
		<#assign x=0>
		<#list columns as column>
			<#if !sortColumn?exists && column == "">
				<#assign selected = true>
			<#elseif sortColumn?exists && column == sortColumn>
				<#assign selected = true>
			<#else>
				<#assign selected = false>		
			</#if>	
			<#include "_listHeader.ftl">
			<#assign x=x+1>
		</#list>
		<th><@s.text name="label.lastlogin"/></th>
		<th></th>
	</tr>
	<#assign count=0 >
	<#list page.list as user>
		<tr id="user_${user.id!}" >
			<#if !isArchivedPage>
				<td>
					<a href="<@s.url action="viewUser" uniqueID="${user.id!}" />" >${(user.userID?html)! }</a>
				</td>
			</#if>
			<td>${user.userLabel?html! }</td>
			<td>${(user.owner.getInternalOrg().name?html)!}</td>
			<td>${(user.owner.customerOrg.name?html)!}&nbsp;</td>
			<td>${(user.owner.divisionOrg.name?html)!}&nbsp;</td>			
			<td>${user.emailAddress?html! } </td>
			
			<td>${(action.dateCreated(user)??)?string(action.formatDateTime(action.dateCreated(user)), "--")}</td>		
			<td>
				<div>
					<#if isArchivedPage>
						<#if user.id != sessionUser.id && !user.admin >
							<#if user.fullUser>
								<@s.url id="unarchiveUrl" action="employeeUserUnarchive" uniqueID="${(user.id)!}" />
							<#elseif user.liteUser>
								<@s.url id="unarchiveUrl" action="liteUserUnarchive" uniqueID="${(user.id)!}" />
							<#else>
								<@s.url  id="unarchiveUrl" action="readOnlyUserUnarchive" uniqueID="${(user.id)!}" />
							</#if>
							<a href="${unarchiveUrl}"/><@s.text name="label.unarchive"/></a>
						</#if>				
					<#else>
						<#if user.fullUser || user.admin>
							<@s.url id="archiveUrl" action="employeeUserArchive" uniqueID="${(user.id)!}" />
							<@s.url id="editUrl" action="employeeUserEdit" uniqueID="${(user.id)!}" />
						<#elseif user.liteUser>
							<@s.url id="archiveUrl" action="liteUserArchive" uniqueID="${(user.id)!}" />
							<@s.url id="editUrl" action="liteUserEdit" uniqueID="${(user.id)!}" />
						<#else>
							<@s.url  id="archiveUrl" action="readOnlyUserArchive" uniqueID="${(user.id)!}" />
							<@s.url  id="editUrl" action="readOnlyUserEdit" uniqueID="${(user.id)!}" />
						</#if>
						<#if user.id != sessionUser.id>
							<a href="${editUrl}"/><@s.text name="label.edit"/></a>
						</#if>
						<#if user.id != sessionUser.id && !user.admin >
							|
							<a href="${archiveUrl}" onclick="return confirm('${action.getText( 'warning.archiveuser',"", (user.userID)! )}');">
								<@s.text name="label.archive" />
							</a>
						</#if>
					</#if>
				</div>
			</td>
		</tr>
	</#list>
</table>