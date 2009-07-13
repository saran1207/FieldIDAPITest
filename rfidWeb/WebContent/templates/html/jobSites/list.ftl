${action.setPageType('job_site', 'list')!}
<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="jobSites.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.customer" /></th>
			<th><@s.text name="label.division" /></th>
			<th><@s.text name="label.datecreated" /></th>
			
		</tr>
		<#list page.getList() as site > 
			<tr id="site_${site.id}" >
				<td><a href="<@s.url action="jobSiteEdit" uniqueID="${site.id}"/>" >${site.name}</td>
				<td>${(site.customer.name)!}</td>
				<td>${(site.division.name)!}</td>
				<td>${action.formatDate(site.created, true)}</td>
				
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyjobsitelist" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="jobSites" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
	


