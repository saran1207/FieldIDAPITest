<head>
	<link rel="StyleSheet" href="/fieldid/style/tabNav.css" type="text/css"/>
	<script type="text/javascript" src="/fieldid/javascript/tabNav.js"></script>
</head>


<ul id="tabnav" class="tabnav">
	<li id="divisions" class="selectedTab"><a href="javascript: void(0);" onclick="toggleTab('tabnav', 'divisions');"><@s.text name="label.divisions"/></a></li>
	<#if securityGuard.partnerCenterEnabled>
		<li id="users" ><a href="javascript: void(0);" onclick="toggleTab('tabnav', 'users');"><@s.text name="label.users"/></a></li>
	</#if>
	
</ul>

<div id="divisions_container">
	<#include "_divisions.ftl"/>
</div>	
<#if securityGuard.partnerCenterEnabled>
	<div id="users_container" style="display:none" >
		<#include "../userCrud/_userList.ftl" />
		<div class="actions">
			<a href="<@s.url value="customerUserEdit.action" uniqueID="" customer="${uniqueID}"/>"><@s.text name="label.addcustomeruser" /></a>
		</div>
		
	</div>	
</#if>