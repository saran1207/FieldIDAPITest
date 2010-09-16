<#assign asset=product/>
var asset = null;
<#include "/templates/html/productCrud/_js_product.ftl"/>

$$('input[name="search"]')[1].clear();
$$('input[name="search"]')[1].focus();

assetFound('${product.serialNumber?html?js_string}', ${product.id}, asset);