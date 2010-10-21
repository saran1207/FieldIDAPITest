var asset = null;
<#include "/templates/html/productCrud/_js_product.ftl"/>

$$('input[name="search"]')[1].clear();
$$('input[name="search"]')[1].focus();

assetFound('${asset.serialNumber?html?js_string}', ${asset.id}, asset);