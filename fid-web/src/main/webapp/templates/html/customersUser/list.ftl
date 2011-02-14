${action.setPageType('customer', 'users')!}

<head>
	<@n4.includeStyle href="user" type="page"/>
</head>

<#if securityGuard.readOnlyUserEnabled>
	<div class="useractions addUser">
		<p><a href="<@s.url action="customersUserAdd" uniqueID=""  customerId="${customer.id}"/>"><@s.text name="label.add_user"/></a></p>
	</div>
</#if>

<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="customersUsers.action" />
	<#include '../common/_pagination.ftl' />
	<#assign userList=page.list />
	<table class="list" id="userList">
		<tr>
			<th ><@s.text name="label.userid"/></th>
	    	<th ><@s.text name="label.username"/></th>
	    	<th ><@s.text name="label.division_name"/></th>
	  		<th ><@s.text name="label.emailaddress"/></th>
	  		<th ><@s.text name="label.lastlogin"/></th>
			<th></th>
		<tr>
		<#assign count=0 >
		<#list userList as user >
			<tr id="user_${user.id!}" >
				<td><a href="<@s.url action="viewUser" uniqueID="${user.id!}" includeParams="get"/>" >${user.userID?html!}</a> </td>
				<td>${user.userLabel?html! }</td>
				<td>${(user.owner.divisionOrg.name)!?html}</td>
				<td>${user.emailAddress?html! } </td>
				<td>${(action.dateCreated(user)??)?string(action.formatDateTime(action.dateCreated(user)), "--")}</td>		
				
				<td><div><a href="<@s.url action="customersUserDelete" uniqueID="${(user.id)!}" includeParams="get"/>"><@s.text name="label.remove" /></a></div></td>
			</tr>
		</#list>
</table>
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistusers" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="customerUsers" currentPage="1"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>