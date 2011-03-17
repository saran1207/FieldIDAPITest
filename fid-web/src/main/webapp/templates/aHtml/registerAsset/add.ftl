<head>
	<@n4.includeStyle href="regNetworkAsset" type="page"/>
	<@n4.includeScript src="asset.js" />
	<@n4.includeScript src="commentTemplates.js" />
	<@n4.includeScript src="updateAttributes.js"/>
	
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_lightView.ftl"/>
	<#include "/templates/html/common/_calendar.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<script type="text/javascript">
	    updateAssetTypeUrl = '<@s.url action="regAssetTypeChange" namespace="/ajax"/>';
	    changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"/>'; 
	    autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"/>';
	    
	    function showSuggestedAttributes() {
	    	var suggestedAttributes = $('suggestedAttributes');
	    	
	    	if(suggestedAttributes != null) {	    	
		    	if($('infoOptions').empty()) {
		    		suggestedAttributes.style.display = "";
		    	}else {
		    		suggestedAttributes.style.display = "none";
		    	}
		    }
	    }
	</script>
</head>
<#include "/templates/html/common/_formErrors.ftl"/>
<div class="form">
	<div id="formHeader">
		
		<div id="imageHolder">
		<#if linkedAsset.type.imageName?exists >
			<img src="<@s.url action="downloadAssetTypeImageSafetyNetwork" namespace="/file" uniqueID="${linkedAsset.type.uniqueID}" networkId="${linkedAsset.networkId}"/>"
			alt="<@s.text name="label.assetimage"/>"/>
		<#else> 
			<img src="<@s.url value="/images/icon-default.png"/>" alt="<@s.text name="label.assetimage"/>" />
		</#if>
		</div>
		<span>
			<h1><@s.text name="label.registerasset"/> - ${linkedAsset.type.name}</h1>
			<p>${linkedAsset.description}</p>
		</span>
	</div>

	<#include '_form.ftl'/>
	
	<div id="fieldidBody"> </div>

</div>