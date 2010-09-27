<head>
	<@n4.includeStyle href="regNetworkAsset" type="page"/>
	<@n4.includeScript src="product.js" />
	<@n4.includeScript src="commentTemplates.js" />
	<@n4.includeScript src="updateAttributes.js"/>
	
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_lightView.ftl"/>
	<#include "/templates/html/common/_calendar.ftl"/>
	<script type="text/javascript">
	    updateProductTypeUrl = '<@s.url action="regProductTypeChange" namespace="/ajax"/>';
	    changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"/>'; 
	    autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"/>';
	</script>
</head>
<#include "/templates/html/common/_formErrors.ftl"/>
<@s.hidden name="currentPage"/>
<@s.hidden name="uniqueID"/>
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
	
	<div id="fieldidBody"> </div>

</div>