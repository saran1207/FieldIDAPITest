${action.setPageType('asset_status', 'list')!}
<#assign statusList = assetStatuses>
<#assign isArchivedList=false>
<#assign emptyMsg="label.emptylistassetstatuses">
<#include "_list.ftl"/>