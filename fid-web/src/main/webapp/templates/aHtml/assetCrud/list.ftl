<#assign namespace="/aHtml"/>
<#assign assetSearchAction="assets"/>
<#assign assetFormId="subAssetSearchForm">
<#assign useOverRides=true/>
<#include "/templates/html/inspectionGroup/_searchForm.ftl"/>


<#assign actionTarget="asset"/>

<#include "/templates/html/inspectionGroup/_assetList.ftl"/>

<style>
	.list {
		width:600px;
	}
</style>