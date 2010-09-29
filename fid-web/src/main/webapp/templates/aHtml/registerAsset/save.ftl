<head>
    <@n4.includeStyle href="regNetworkAsset" type="page"/>
	<#include "/templates/html/common/_lightView.ftl"/>
</head>

<div id="confirm" class="center">
	<div class="confirmBody">
	<p><img src="<@s.url value="/images/register-checkmark.png"/>" /></p>
	<h1>Asset Registered</h1>
	<p><@s.text name="label.registerconfirm"/> - ${linkedProduct.type.name}</p>
	<div id="links">
		<p><@s.text name="label.whatsnext"/></p>
		<p><a href="" onclick="closeLightBox();window.parent.location.replace('<@s.url value="/inspectionGroups.action" uniqueID="${newProduct.id}"/>')" ><@s.text name="label.performfirstevent"/></a></p>
		<p><a href="" onclick="closeLightBox();window.parent.location.replace('<@s.url value="/product.action" uniqueID="${newProduct.id}"/>')"> <@s.text name="label.viewassetinfo"/></a></p>
	</div>
	<p>
		<button onclick="closeLightBox();window.parent.location.reload(true);return false;"><@s.text name="label.ok"/></button>
	</p>
	<div>
</div>