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
	    
	    function showSuggestedAttributes() {
	    	var suggestedAttributes = $('suggestedAttributes');
	    	var infoOptions = $('infoOptions');
	    	if(infoOptions.empty()) {
	    		suggestedAttributes.style.display = "";
	    	}else {
	    		suggestedAttributes.style.display = "none";
	    	}
	    }
	</script>
</head>
<#include "/templates/html/common/_formErrors.ftl"/>
<div class="form">
	<div id="formHeader">
		
		<div id="imageHolder">
		<#if linkedProduct.type.imageName?exists >	
			<img src="<@s.url action="downloadProductTypeImageSafetyNetwork" namespace="/file" uniqueID="${linkedProduct.type.uniqueID}" networkId="${linkedProduct.networkId}"/>" 
			alt="<@s.text name="label.productimage"/>"/>
		<#else> 
			<img src="<@s.url value="/images/icon-default.png"/>" alt="<@s.text name="label.productimage"/>" />
		</#if>
		</div>
		<span>
			<h1><@s.text name="label.registerasset"/> - ${linkedProduct.type.name}</h1>
			<p>${linkedProduct.description}</p>
		</span>
	</div>

	<#include '_form.ftl'/>
	
	<div id="fieldidBody"> </div>

</div>