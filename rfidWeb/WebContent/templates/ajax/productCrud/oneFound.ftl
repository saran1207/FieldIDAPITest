<#assign asset=product/>
var asset = null;
<#include "/templates/html/productCrud/_js_product.ftl"/>

assetFound('${product.serialNumber?html?js_string}', ${product.id}, asset);