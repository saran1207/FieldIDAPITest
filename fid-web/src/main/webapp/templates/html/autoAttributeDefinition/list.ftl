${action.setPageType('auto_attribute', 'definitions')!}
<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>

<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="autoAttributeDefinitionList.action" >
	<#include '../common/_pagination.ftl' >

	<table class="list">
		<tr>
			<#list autoAttributeCriteria.inputs as input  >
				<th>${input.name}</th>
			</#list>
			<th></th>
		<tr>
		
		<#list page.list as definition >
			<tr>
				<#list definition.inputs as input  >
					<td>${input.name! } </td>
				</#list>
				<td>
					<div><a href="<@s.url action="autoAttributeDefinitionEdit" uniqueID="${definition.id!}" criteriaId="${criteriaId}" />" ><@s.text name="label.edit" /></a></div>
					<div><a href="<@s.url action="autoAttributeDefinitionRemove" uniqueID="${(definition.id)!}" criteriaId="${criteriaId}"/>" ><@s.text name="label.remove" /></a></div>
				</td>
			</tr>
		</#list>
		
	</table>
	<#include '../common/_pagination.ftl' >
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistautoattribute" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url action="autoAttributeDefinitionList" criteriaId="${criteriaId}" />" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
	
