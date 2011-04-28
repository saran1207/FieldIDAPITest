<head>
	<script type="text/javascript" >
		function createSuper( id ) {
			var div = $( "superUser_" + id );
			div.style.display = "block";
			return false;
		}
		function submitSuperUser( button ) {
			button.form.request( { 
				onComplete: 
					function( response ) {
						try { 
							var forms = document.getElementsByClassName( "superUserForm" );
							for( var i = 0; i < forms.length; i++ ) {
								forms[i].style.display = "none";
								
							}
						} catch( e ) {
							alert( e );
						}
					}
			} );
		}
		
		function clearForm() {
			$('nameFilter').value = ''
			$('listFilterForm').submit();	
		}
	
	</script>
</head>
<div id="orgListFilter">
	<@s.form id="listFilterForm" method="get" theme="simple"> 
		<@s.hidden name="currentPage" value="1"/>
		<label>Filter by Company Name</label>
		<@s.textfield id="nameFilter" name="nameFilter"/>
		<@s.submit name="search" key="hbutton.filter" />
		<span><@s.text name="label.or" /></span>
		<a href="javascript:void(0);" onClick="clearForm();"> <@s.text name="hbutton.clear"/></a>
	</@s.form>
</div>

<#include "../../../../templates/html/common/_pagination.ftl">
<table class="orgList">
	<tr>
		<#assign columns = ["tenant.disabled", "name", "tenant.name", "", "", "", "", "", "", "created"] >
		<#assign labels = ["Active", "Company Name", "Company ID", "Total Assets", "Assets Last 30 Days", "Total Events", "Events Last 30 Days", "Last Login Date", "Last Login User", "Created"] >
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
			<td>${primaryOrg.displayName?html}</td>
			<td><a href='${action.getLoginUrlForTenant(primaryOrg.tenant)}' target='_blank'>${primaryOrg.tenant.name!}</a></td>
			<td>${action.getTotalAssets(primaryOrg.id)?string.number}</td>
			<td>${action.getTotal30DayAssets(primaryOrg.id)?string.number}</td>
			<td>${action.getTotalEvents(primaryOrg.id)?string.number}</td>
			<td>${action.getTotal30DayEvents(primaryOrg.id)?string.number}</td>	
			<#if action.getLastActiveSession(primaryOrg.id)?exists && action.getLastActiveSession(primaryOrg.id).user.userID != 'n4systems'>
				<td>${action.getLastActiveSession(primaryOrg.id).lastTouched?datetime}</td>
				<td>${action.getLastActiveSession(primaryOrg.id).user.userID}</td>
			<#else>
				<td>--</td>
				<td>--</td>
			</#if>
			<td>${primaryOrg.created?date}</td>
			<td><a href="<@s.url namespace="/admin" action="organizationEdit"/>?id=${primaryOrg.tenant.id}">Edit</a></td>
		</tr>
	</#list>
</table>
<#include "../../../../templates/html/common/_pagination.ftl">
<p>
	Total Tenants: ${page.totalResults}
</p>
