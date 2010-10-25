<#assign namespace="/aHtml"/>
<#assign assetSearchAction="products"/>
<#assign assetFormId="subAssetSearchForm">
<#assign useOverRides=true/>
<#include "/templates/html/inspectionGroup/_searchForm.ftl"/>


<#assign actionTarget="product"/>

<#include "/templates/html/inspectionGroup/_productList.ftl"/>

<style>
	.list {
		width:600px;
	}
</style>