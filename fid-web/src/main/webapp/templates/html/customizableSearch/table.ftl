<#if resultsTable?exists >

<#include '_rowSelectionPageHeader.ftl' />

<#include '../common/_newpagination.ftl' />
<div class="selectionNotice">
    <#include '_selectionStatus.ftl'>
</div>
<table id="resultsTable" class="list">
    <thead>
        <tr>
            <#if preRowHeaderTemplate?exists ><#include "${preRowHeaderTemplate}" /></#if>

            <th class="checkBox">
                <#include "_rowSelectionTableHeader.ftl"/>
            </th>

            <#list selectedColumns as columnId >
                <#assign column_class="" />
                <#assign columnPath=action.getColumnMapping(columnId).dbColumnId!0 />
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
                        <a href="${sortUrl}" onClick="clearSelection('${searchContainerKey}', '${searchId}')"><@s.text name="${columnLabel}"/></a>
                    <#else>
                        <@s.text name="${columnLabel}"/>
                    </#if>
                </th>
            </#list>

            <#if postRowHeaderTemplate?exists ><#include "${postRowHeaderTemplate}" /></#if>
        </tr>
    </thead>
    <tbody>
        <#list 0..resultsTable.rowSize -1 as rowIdx>

            <#assign entityId="${resultsTable.getId(rowIdx)}" />
            <#assign rowClass="${action.getRowClass(rowIdx)!}" />

            <tr <#if rowClass?exists >class="${rowClass}"</#if> id="row-${entityId}">
                <#if preRowTemplate?exists ><#include "${preRowTemplate}" /></#if>

                <td class="checkBox">
                    <#include "_rowSelectionTableCell.ftl">
                </td>

                <#list 0..resultsTable.columnSize -1 as colIdx>
                    <td id="${selectedColumns.get(colIdx)}_${rowIdx}">${action.getCell(rowIdx, colIdx)}&nbsp;</td>
                </#list>

                <#if postRowTemplate?exists ><#include "${postRowTemplate}" /></#if>
            </tr>
        </#list>
    </tbody>
</table>
<#include '../common/_newpagination.ftl' />

<script type="text/javascript">
    <#list resultsTable.idList as itemId>
        itemIdsOnCurrentPage.push(#{itemId});
    </#list>
</script>
</#if>