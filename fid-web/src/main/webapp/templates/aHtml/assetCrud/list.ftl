<#assign namespace="/aHtml"/>
<#assign assetSearchAction="assets"/>
<#assign assetFormId="subAssetSearchForm">
<#assign useOverRides=true/>
<#assign usePaginatedResults=false/>
<#include "/templates/html/eventGroup/_searchForm.ftl"/>

<#assign actionTarget="asset"/>

<#include "/templates/html/eventGroup/_assetListNonPaginated.ftl"/>

<style>
	.list {
		width:600px;
	}
</style>