${action.setPageType('asset_status', 'list_archived')!}
<#assign statusList = archivedAssetStatuses>
<#assign isArchivedList=true>
<#assign emptyMsg="label.emptylistarchivedassetstatuses">
<#include "_list.ftl"/>