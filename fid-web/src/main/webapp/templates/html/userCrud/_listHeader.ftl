<#if selected && (sortDirection?exists && sortDirection = "asc")>			
	<#assign sortDisplay = "sorted down" />
<#elseif selected && (sortDirection?exists && sortDirection = "desc")>
	<#assign sortDisplay = "sorted up" />
<#elseif selected && !sortDirection?exists>
	<#assign sortDisplay = "sorted up" />
<#else>
	<#assign sortDisplay = "" />
</#if> 

<#if selected && !sortDirection?exists>
	<#assign direction = "desc"/>			
<#elseif selected && sortDirection = "asc">			
	<#assign direction = "desc"/>
<#elseif (selected && sortDirection = "desc") || !selected>
	<#assign direction = "asc"/>			
</#if> 

<th class="${sortDisplay}">
	<a href='<@s.url action="${sortAction}" sortColumn="${column}" sortDirection="${direction}"/>'>
		${labels[x]}
	</a>
</th>