<head>
	<@n4.includeStyle href="registerAsset" type="page"/>
	<@n4.includeScript src="product.js" />
	<#include "/templates/html/common/_orgPicker.ftl"/>
		<script type="text/javascript">
	    updateProductTypeUrl = '<@s.url action="productTypeChange" namespace="/ajax"/>';
	</script>
</head>
<div class="form">

	<div id="formHeader">
		<h1><@s.text name="label.registerasset"/> - ${linkedProduct.serialNumber}</h1>
		
		<#if linkedProduct.type.imageName?exists >	
			<img src="<@s.url action="downloadProductTypeImageSafetyNetwork" namespace="/file" uniqueID="${linkedProduct.type.uniqueID}" networkId="${linkedProduct.networkId}"/>" 
			alt="<@s.text name="label.productimage"/>"/>
		</#if>
		<span>
			<p>${linkedProduct.type.name}</p>
			<p>${linkedProduct.description}</p>
		</span>
	</div>

	<#include '_form.ftl'/>

</div>