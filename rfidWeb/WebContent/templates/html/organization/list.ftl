${action.setPageType('organization','list')!}
	<@s.url action="organizations" id="pageAction"/>
	
	<h2 class="sectionTitle">
		<@s.text name="label.primaryorganization"/>
		<a href="<@s.url action="organizationEdit" uniqueID="${primaryOrg.id}"/>"><@s.text name="label.edit"/></a>
	</h2>
	<div class="multiColumn fluidSets">
		<div class="infoSet infoBlock">
			<label for="name" class="label"><@s.text name="label.name"/></label>
			<span class="fieldHolder">${primaryOrg.name?html}</span>
		</div>
		<div class="infoSet infoBlock">
			<label for="name" class="label"><@s.text name="label.name_on_cert"/></label>
			<span class="fieldHolder">
				${primaryOrg.certificateName?html}	
			</span>
		</div>
	</div>
	
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



