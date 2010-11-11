${action.setPageType('customer', 'divisions')!}
<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>

<#if page.hasResults() && page.validPage() >
	<#assign currentAction="divisions.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.division_id" /></th>
			<th></th>
		</tr>
		<#list page.getList() as division > 
			<tr id="division_${division.id}" >
				<td>
					<a href="<@s.url action="divisionEdit" uniqueID="${division.id}" includeParams="get"/>" >${(division.name?html)!}</a>
				</td>
				<td>${(division.code?html)!}</td>
				<td>
					<a href="<@s.url action="divisionEdit" uniqueID="${division.id}" includeParams="get"/>"><@s.text name="label.edit"/></a> | 
					<a href="<@s.url action="divisionArchive" uniqueID="${division.id}" includeParams="get"/>"><@s.text name="label.archive"/></a>
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

<#if archivedPage.hasResults() && archivedPage.validPage() >
	<div>
		<img id="plus" src="<@s.url value="/images/plus.png"/>" alt="+" />
		<img id="minus" src="<@s.url value="/images/minus.png"/>" alt="-" style="display:none"/>
		<a id="expand_archived_list" onclick="openSection('archivedList', 'expand_archived_list', 'collapse_archived_list');plus.style.display='none';minus.style.display='';return false" href="javascript:void(0);" ><@s.text name="label.showarchiveddivisions"/></a>
		<a id="collapse_archived_list" onclick="closeSection('archivedList', 'collapse_archived_list', 'expand_archived_list');plus.style.display='';minus.style.display='none';return false" href="javascript:void(0);" style="display:none"><@s.text name="label.hidearchiveddivisions"/></a>
	</div>

	<div id="archivedList" style="display:none">
		<#include '../common/_pagination.ftl' />
		<table class="list">
			<tr>
				<th><@s.text name="label.name" /></th>
				<th><@s.text name="label.division_id" /></th>
				<th></th>
			</tr>
			<#list archivedPage.getList() as division > 
				<tr id="division_${division.id}" >
					<td>${(division.name?html)!}</td>
					<td>${(division.code?html)!}</td>
					<td>
						<a href="<@s.url action="divisionUnarchive" uniqueID="${division.id}" includeParams="get"/>"><@s.text name="label.unarchive"/></a>
					</td>
				</tr>	
			</#list>
		</table>
		
		<#include '../common/_pagination.ftl' />
	</div>	
</#if>