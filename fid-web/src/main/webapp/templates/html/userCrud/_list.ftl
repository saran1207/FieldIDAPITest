<head>
	<@n4.includeStyle href="user" type="page"/>
	<@n4.includeStyle href="listFilter" type="page"/>
	<script type="text/javascript">
			function clearForm() {
			$('nameFilter').value = ''
			$('userGroup').selectedIndex = 0;	
			$('userType').selectedIndex = 0;	
			$('orgFilter').selectedIndex = 0;
			$('listFilterForm').submit();	
		}
	</script>
</head>
<div class="listFilter quickForm">
	<div id="listFilterHeader">
		<h2><@s.text name="label.filter" /></h2>
		&nbsp;
		<span class="egColor"><@s.text name="message.filter_users"/></span>
	</div>
	<@s.form id="listFilterForm" method="get"> 
		<@s.hidden name="currentPage" value="1"/>

		<@s.textfield id="nameFilter" name="listFilter" key="label.name" labelposition="left"/>

		<@s.select id="userGroup" name="userGroup" list="userGroups" listKey="id" listValue="name" key="label.usergroup" labelposition="left"/>
		
		<#if securityGuard.readOnlyUserEnabled>
			<@s.select cssClass="userTypeSelect" id="userType" name="userType" list="userTypes" listKey="id" listValue="name" key="label.usertype" labelposition="left"/>
		</#if>
		
		<@s.select key="label.organization" name="orgFilter" id="orgFilter" list="parentOrgs" listKey="id" listValue="name" headerKey="" headerValue="All" labelposition="left"/>
		
		<div class="formAction">
			<@s.submit name="search" key="hbutton.filter" />
			<span><@s.text name="label.or" /></span>
			<a href="javascript:void(0);" onClick="clearForm();"> <@s.text name="hbutton.clear"/></a>
		</div>
	</@s.form>
</div>

<#if page.hasResults() && page.validPage()>
	<#if isArchivedPage>
		<#assign currentAction="archivedUserList.action" />
	<#else>
		<#assign currentAction="userList.action" />
	</#if>
	<#include '../common/_pagination.ftl' />
	<#include "_userList.ftl" />
	<#include '../common/_pagination.ftl' />
	<div class="total"><@s.text name="label.total"/>:&nbsp;${page.totalResults}</div>
<#elseif !page.hasResults()>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistusers" />
		</p>
	</div>
<#else>
	<script type="text/javascript">
		<@s.url  action="userList" currentPage="1" id="url"/>
		window.location.href = '${url}';
	</script>
</#if>