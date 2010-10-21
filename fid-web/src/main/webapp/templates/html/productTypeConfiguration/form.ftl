<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/productTypeConfiguration.css"/>" />
	<script type="text/javascript" src="<@s.url value="/javascript/products/productTypeConfiguration.js" />" ></script>
	<script type="text/javascript">
		numberOfSubTypes = ${subProductIds?size};
		removeString = '<@s.text name="label.remove"/>';
	</script>
</head>

${action.setPageType('product_type', 'configuration')!}
<#if partOfMasterProduct >
	<div class="formErrors error" >
		 <@s.text name="instruction.typealreadyusedbyparent"><@s.param >${assetType.name}</@s.param></@s.text>
	</div>
		
<#else>
	<div class="instruction">
		<h3><span class="instructionTitle"><@s.text name="label.tellushowtoconfigure"/>&nbsp;</span><span>${assetType.name}</span></h3>
		<p >
			<@s.text name="instruction.productconfiguration">
				<@s.param>${assetType.name}</@s.param>
			</@s.text>
		</p>
	</div>
	<@s.form action="productTypeConfigurationUpdate" theme="fieldidSimple" cssClass="crudForm">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<@s.hidden name="uniqueID" />
		<h3><@s.text name="label.A"/> ${assetType.name} <@s.text name="label.has" />: </h3>
		<ul id="subProducts">
			<#list subProducts as subProduct >
				<li id="subProduct_${subProduct.id}">
					<@s.hidden name="subProductIds[${subProduct_index}]" value="${subProduct.id}" />
					<span id="productName_${subProduct.id}">${subProduct.name}</span>  
					<a id="removeProductLink_${subProduct.id}" href="removeSubProduct" assetTypeId="${subProduct.id}"  ><@s.text name="label.remove"/></a>
					<script type="text/javascript" >
						$('removeProductLink_${subProduct.id}').observe('click', removeProductEvent);
					</script>
				</li>
			</#list>
		</ul>
		<div id="addSubProductContainer">
			<span> <@s.select id="addSubProduct" name="addSubProduct" list="assetTypes" listKey="id" listValue="name"  /></span>
			<@s.submit id="addSubProductButton" key="label.add" type="button" />
		</div>
		
		<div class="formAction">
			<@s.url id="cancelUrl" action="productType" uniqueID="${uniqueID}"/>
			<@s.submit key="label.cancel" onclick="return redirect( '${cancelUrl}' );" />
			<@s.submit key="label.save"/>
		</div>
	
	</@s.form>
		<script type="text/javascript" >
			$('addSubProductButton').observe('click', includeSubProduct);
		</script>
</#if>