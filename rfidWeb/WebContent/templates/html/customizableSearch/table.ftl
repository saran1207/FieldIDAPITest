<#if resultsTable?exists >

<#include '../common/_newpagination.ftl' />
<table id="resultsTable" class="list">
	<tr>
		<#if preRowHeaderTemplate?exists ><#include "${preRowHeaderTemplate}" /></#if>
		
		<#list selectedColumns as columnId >
			<#assign column_class="" />
			<#assign columnPath="${action.getColumnMapping(columnId).pathExpression}" />
			<#assign columnLabel="${action.getColumnMapping(columnId).label}" />
			
			<#if sortColumn?exists && columnPath == sortColumn>
				<#switch sortDirection >
					<#case "desc" >
						<#assign column_class="sorted up" />
						<#assign linkDirection="asc">	
					<#break>
					<#case "asc" >
						<#assign column_class="sorted down" />
						<#assign linkDirection="desc">
					<#break>
				</#switch>
			<#else>
				<#assign linkDirection="asc">
			</#if>
		
			<th class="${column_class}">
				<#if action.getColumnMapping(columnId).sortable >
					<@s.url id="sortUrl" action="${currentAction}" includeParams="get" sortDirection="${linkDirection}" sortColumn="${columnPath}" />	
					<a href="${sortUrl}"><@s.text name="${columnLabel}"/></a>
				<#else>
					<@s.text name="${columnLabel}"/>
				</#if>
			</th>
		</#list>
		
		<#if postRowHeaderTemplate?exists ><#include "${postRowHeaderTemplate}" /></#if>
	</tr>
	<#list 0..resultsTable.rowSize -1 as rowIdx>
		<#assign entityId="${resultsTable.getId(rowIdx)}" />
		<#assign rowClass="${action.getRowClass(rowIdx)!}" />
		
		<tr <#if rowClass?exists >class="${rowClass}"</#if> >
			<#if preRowTemplate?exists ><#include "${preRowTemplate}" /></#if>
		
			<#list 0..resultsTable.columnSize -1 as colIdx>
				<td id="${selectedColumns.get(colIdx)}_${rowIdx}">${action.getCell(rowIdx, colIdx)}</td>
			</#list>
			
			<#if postRowTemplate?exists ><#include "${postRowTemplate}" /></#if>
		</tr>
	<tr>
	</#list>
</table>
<#include '../common/_newpagination.ftl' />
</#if>