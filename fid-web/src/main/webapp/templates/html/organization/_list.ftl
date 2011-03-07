<#if  page.hasResults() && page.validPage() >
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.organizationalunits"/></th>
			<th>&nbsp;</th>
		</tr>
		<#list page.getList() as organization > 
			<tr id="organization_${organization.id}" >
				<td>${organization.name?html}</td>
				<td>
					<a href="<@s.url action="organizationEdit" uniqueID="${organization.id}"/>"><@s.text name="label.edit"/></a>
					<#if organization.active>
						<a href="<@s.url action="organizationConfirmArchive" uniqueID="${organization.id}"/>"><@s.text name="label.archive"/></a>
					<#else>
						<a href="<@s.url action="organizationUnarchive" uniqueID="${organization.id}"/>"><@s.text name="label.unarchive"/></a>
					</#if>
				</td>
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyorganizationlist" /> <#if !archivedList><@s.text name="label.emptyorganizationlistinstruction" /></#if>
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="oragnizations" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>