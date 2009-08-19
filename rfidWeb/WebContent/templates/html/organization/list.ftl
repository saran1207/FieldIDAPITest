${action.setPageType('organization','list')!}
<#if  page.hasResults() && page.validPage() >
	<@s.url action="organizations" id="pageAction"/>
	
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.name"/></th>
			<th>&nbsp;</th>
		</tr>
		<#list page.getList() as organization > 
			<tr id="organization_${organization.id}" >
				<td>${organization.name?html}</td>
				<td>
					<a href="<@s.url action="organizationEdit" uniqueID="${organization.id}"/>"><@s.text name="label.edit"/></a>
				</td>
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyorganizationlist" /> <@s.text name="label.emptyorganizationlistinstruction" />
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



