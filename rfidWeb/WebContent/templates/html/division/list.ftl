${action.setPageType('customer', 'divisions')!}

<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>

<#if page.hasResults() && page.validPage() >
	<#assign currentAction="divisions.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.division_id" /></th>
			<th><@s.text name="label.name" /></th>
			<th></th>
		</tr>
		<#list page.getList() as division > 
			<tr id="division_${division.id}" >
				<td><a href="<@s.url action="divisionEdit" uniqueID="${division.id}" includeParams="get"/>" >${(division.divisionID?html)!}</a></td>
				<td>${(division.name?html)!}</td>
				<td>
					<a href="<@s.url action="divisionEdit" uniqueID="${division.id}" includeParams="get"/>"><@s.text name="label.edit"/></a>
					<a href="<@s.url action="divisionDelete" uniqueID="${division.id}" includeParams="get"/>"><@s.text name="label.delete"/></a>
				</td>
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptydivisionlist" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="divisions" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>