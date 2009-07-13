<div id="infoOptions">
	
	<#assign fieldPrefix= 'product' />
	<#assign useAutoAttributes=true />
	<#if autoAttributeCriteria?exists >
		<#assign autoAttributeInputFields=autoAttributeCriteria.inputs />
	</#if>
	<#assign prefix= 'product' />
	<#assign requires='true'>
	<@s.fielderror>
		<@s.param>productInfoOptions</@s.param>				
	</@s.fielderror>
	<#include "/templates/html/common/_dynamicOptions.ftl" />
	
</div>
