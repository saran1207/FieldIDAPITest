${action.setPageType('organization','list')!}
<@s.url action="organizations" id="pageAction"/>
	
<h2 class="decoratedHeader">
	<@s.text name="label.primaryorganization"/>
	<a href="<@s.url action="organizationEdit" uniqueID="${primaryOrg.id}"/>"><@s.text name="label.edit"/></a>
</h2>
<div class="multiColumn fluidSets">
	<div class="infoSet infoBlock borderLess">
		<label for="name" class="label"><@s.text name="label.name"/></label>
		<span class="fieldHolder">${primaryOrg.name?html}</span>
	</div>
	<div class="infoSet infoBlock borderLess">
		<label for="name" class="label"><@s.text name="label.name_on_pdf_reports"/></label>
		<span class="fieldHolder">
			${primaryOrg.certificateName?html}	
		</span>
	</div>
</div>
<#assign archivedList=false>	
<#include '_list.ftl' />	