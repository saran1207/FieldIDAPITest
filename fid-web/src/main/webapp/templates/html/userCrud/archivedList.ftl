${action.setPageType('user','list_archived')!}

<#if archivedPage.hasResults() && archivedPage.validPage() >
	<#assign page=archivedPage >
	<#include '../common/_pagination.ftl' />
	<table class="list" id="userList">
		<tr>
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
		<#list page.list as user >
			<tr id="user_${user.id!}" >
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
							<@s.url id="unarchiveUrl" action="employeeUserUnarchive" uniqueID="${(user.id)!}" />
						<#elseif user.liteUser>
							<@s.url id="unarchiveUrl" action="liteUserUnarchive" uniqueID="${(user.id)!}" />
						<#else>
							<@s.url  id="unarchiveUrl" action="readOnlyUserUnarchive" uniqueID="${(user.id)!}" />
						</#if>
						<div><a href="${unarchiveUrl}"/><@s.text name="label.unarchive"/></a></div>
					</#if>
				</td>
			</tr>
		</#list>
	</table>
	<#include '../common/_pagination.ftl' />
<#elseif !archivedPage.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistusers" />
		</p>
	</div>
<#else>
	<script type="text/javascript">
		<@s.url  action="archivedUserList" currentPage="1" id="url"/>
		window.location.href = '${url}';
	</script>
</#if>