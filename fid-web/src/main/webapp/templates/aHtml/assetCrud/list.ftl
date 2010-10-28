<#assign namespace="/aHtml"/>
<#assign assetSearchAction="assets"/>
<#assign assetFormId="subAssetSearchForm">
<#assign useOverRides=true/>
<#include "/templates/html/eventGroup/_searchForm.ftl"/>


<#assign actionTarget="asset"/>

<#include "/templates/html/eventGroup/_assetList.ftl"/>

<style>
	.list {
		width:600px;
	}
</style>