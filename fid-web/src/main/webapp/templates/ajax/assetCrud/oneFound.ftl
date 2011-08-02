var asset = null;
<#include "/templates/html/assetCrud/_js_asset.ftl"/>

$$('input[name="search"]')[1].clear();
$$('input[name="search"]')[1].focus();

assetFound('${asset.identifier?html?js_string}', ${asset.id}, asset);