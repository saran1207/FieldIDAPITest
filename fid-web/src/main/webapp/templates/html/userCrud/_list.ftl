<head>
	<script type="text/javascript">
		document.observe("dom:loaded", function() {
			updateUserTypes($('userGroup'));
		});
	</script>
</head>
<div class="quickForm">
	<h3><@s.text name="label.filter" /></h3>
	<@s.form method="GET" cssClass="simpleInputForm" theme="css_xhtml" > 
		<@s.hidden name="currentPage" value="1"/>
		<@s.textfield id="nameFilter" name="listFilter" key="label.name" labelposition="left"/>
		
		<@s.select id="userGroup" name="userGroup" list="userGroups" onchange="updateUserTypes(this)" listKey="id" listValue="name" key="label.usergroup" labelposition="left"/>
		<#if securityGuard.readOnlyUserEnabled>
			<@s.select cssClass="userTypeSelect" id="userType" name="userType" list="userTypes" listKey="id" listValue="name" key="label.usertype" labelposition="left"/>
		</#if>
		
		<div class="filterActions">
			<@s.submit name="search" key="hbutton.search" />
			<@s.submit name="clear" key="hbutton.clear" onclick="$('nameFilter').value='';"/>
		</div>
	</@s.form>
</div>

<#if page.hasResults() && page.validPage() && !userList.empty>
	<#assign currentAction="userList.action" />
	<#include '../common/_pagination.ftl' />
	<#assign userList=userList />
	<#include "_userList.ftl" />
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() || userList.empty>
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

<#include '../userCrud/_userGroupChangeScript.ftl'/>