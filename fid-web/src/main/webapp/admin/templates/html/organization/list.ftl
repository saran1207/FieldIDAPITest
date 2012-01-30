<head>
    ${action.setPageName('tenants')!}
	<script type="text/javascript" >
		function clearForm() {
			$('nameFilter').value = '';
            $('activeOnly').checked = false;
            $('inactiveSince').selectedIndex = 0;
            $('listFilterForm').submit();
		}
	
	</script>
</head>

<input class="floatRight" type="button" onClick="window.location='/fieldid/w/admin/addTenant'" value="Create a new Tenant" />

<div id="orgListFilter">
	<@s.form id="listFilterForm" method="get" theme="simple"> 
		<@s.hidden name="currentPage" value="1"/>
		<label>Filter by Company Name</label>
		<@s.textfield id="nameFilter" name="nameFilter"/>
		<@s.submit name="search" key="hbutton.filter" />
		<span><@s.text name="label.or" /></span>
		<a href="javascript:void(0);" onClick="clearForm();"> <@s.text name="hbutton.clear"/></a>
        <br/>
        <br/>
        Inactive for: <@s.select id="inactiveSince" name="inactiveSince" list="dateRanges" emptyOption="true" listKey="id" listValue="displayName"/>
        Active only: <@s.checkbox id="activeOnly"  name="activeOnly" />
	</@s.form>
</div>

<#include "../../../../templates/html/common/_pagination.ftl">
<table class="orgList">
	<tr>
		<#assign columns = ["tenant.disabled", "name", "tenant.name", "", "", "", "", "created"] >
		<#assign labels = ["Active", "Company Name", "Company ID", "Assets Last 30 Days","Events Last 30 Days", "Last Login Date", "Last Login User", "Created"] >
		<#assign sortAction = "organizations" >
		<#assign x=0>
		<#list columns as column>
			<#if !sortColumn?exists>
				<#assign selected = true>
			<#elseif sortColumn?exists && column == sortColumn>
				<#assign selected = true>
			<#else>
				<#assign selected = false>		
			</#if>	
			
			<#if column == "">
				<th>${labels[x]}</th>
			<#else>
				<#include "_listHeader.ftl">
			</#if>
			<#assign x=x+1>
		</#list>
		<th></th>
	</tr>
	<#list page.list as primaryOrg>
		<tr>
			<td <#if primaryOrg.tenant.disabled> class='offIcon' <#else> class='onIcon'</#if>></td>
			<td><a href="<@s.url namespace="/admin" action="organizationEdit"/>?id=${primaryOrg.tenant.id}">${primaryOrg.displayName?html}</a></td>
			<td>${primaryOrg.tenant.name!}</td>

			<td>${action.getTotal30DayAssets(primaryOrg.id)?string.number}</td>
			<td>${action.getTotal30DayEvents(primaryOrg.id)?string.number}</td>
			<#if primaryOrg.tenant.lastLoginUser?exists && primaryOrg.tenant.lastLoginUser.userID != 'n4systems'>
				<td>${primaryOrg.tenant.lastLoginTime?datetime}</td>
				<td>${primaryOrg.tenant.lastLoginUser.userID}</td>
			<#else>
				<td>--</td>
				<td>--</td>
			</#if>

			<td>${primaryOrg.created?date}</td>
		</tr>
	</#list>
</table>
<#include "../../../../templates/html/common/_pagination.ftl">
<p>
	Total Tenants: ${page.totalResults}
</p>
