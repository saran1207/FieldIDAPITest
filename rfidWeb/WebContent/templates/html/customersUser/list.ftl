${action.setPageType('customer', 'users')!}
<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>

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
			<th></th>
		<tr>
		<#assign count=0 >
		<#list userList as user >
			<tr id="user_${user.id!}" >
				<td><a href="<@s.url action="customersUserEdit" uniqueID="${user.id!}" includeParams="get"/>" >${user.userID?html!}</a> </td>
				<td>${user.userLabel?html! }</td>
				<td>${(user.owner.divisionOrg.name)!?html}</td>
				<td>${user.emailAddress?html! } </td>
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