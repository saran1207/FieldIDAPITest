<head>
	<script type="text/javascript" src="<@s.url value="/javascript/product.js"/>" ></script>
	<script type='text/javascript' src='<@s.url value="/javascript/productRfidHandler.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateSerialNumber.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/combobox.js"/>"></script>
	<script type='text/javascript' src='<@s.url value="/javascript/updateAttributes.js"/>'></script>
	<script type="text/javascript" src="<@s.url value="/javascript/generateSerialNumber.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/subProduct.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/inspection.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/masterInspection.js"/>"></script>
	<@n4.includeStyle type="page" href="product" />
	<@n4.includeStyle type="page" href="subProduct" />
	<@n4.includeStyle type="page" href="inspection" />
	<@n4.includeStyle type="page" href="masterInspection" />
	
	<#include "/templates/html/common/_calendar.ftl"/>
	
	<script type="text/javascript">
		lookupProductUrl = "<@s.url action="products" namespace="/ajax"/>";
		productLookupTitle = "<@s.text name="title.productlookup"/>";
		subProductIndex = ${subProducts?size};
		updateProductTypeUrl = '<@s.url action="productTypeChange" namespace="ajax"  />';
		addSubProductUrl = '<@s.url action="createSubProductInInspection" namespace="/ajax" />';
		addProductUrl = '<@s.url action="productAdd" namespace="/ajax"/>';
		createProductUrl = "<@s.url action="productCreate" namespace="/ajax" />";	
		autoAttributeUrl = '<@s.url action="autoAttributeCriteria" namespace="/ajax"  />';
		serialNumberUrl = '<@s.url action="generateSerialNumber" namespace="/aHtml"  />';
		checkRfidUrl = '<@s.url action="checkRFID" namespace="/ajax"  />';
		removeSubProductUrl = "<@s.url action="removeSubProduct" namespace="/ajax"/>";
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
		checkSerialNumberUrl = '<@s.url action="checkSerialNumber" namespace="/ajax" />';
		
	</script>
</head>
${action.setPageType('inspection', 'add')!}

<div id="masterInspection" >
	<#include "/templates/html/common/_formErrors.ftl" />
	<div class="inspectionHeader">
		<h3><@s.text name="label.perform_event"/></h3>
		<p class="instructions smallInstructions">
			<@s.text name="instructions.master_inspection"/>
		</p>
	</div>
	
	<div class="masterProduct <#if masterInspection.mainInspectionStored >done</#if>">
		<div class="definition"><div class="identifier"><span>${product.type.name!}</span></div></div>
		<div class="performedInspection">
			<span>${(inspectionType.name)!}</span> 
			<span>
				<a class="exitLink" href="<@s.url action="subInspectionAdd" uniqueID="0" productId="${product.id}" type="${type}" parentProductId="${product.id}" token="${token}" scheduleId="${scheduleId!}" />">
					<#if !masterInspection.mainInspectionStored >
						<@s.text name="label.you_must_perform_this_event"/>
					<#else>
						<@s.text name="label.edit_this_event"/>
					</#if>
				</a>
			</span>
		</div>
	</div>

	<div id="productComponents">
		<#list product.subProducts as subProduct >
			<#include "_subProductInspection.ftl" />
		</#list>
	</div>
	
	<@s.form action="masterInspectionCreate" id="subProductForm" cssClass="crudForm" theme="fieldid">
		<@s.hidden name="uniqueID" id="uniqueID"/>
		<@s.hidden name="token" id="searchToken"/>
		<@s.hidden name="type"/>
		<@s.hidden name="inspectionGroupId"/>
		<@s.hidden name="productId" id="productId"/>
		<@s.hidden name="cleanToInspectionsToMatchConfiguration" />
		<div class="formAction">
			<@s.url id="cancelUrl" action="inspectionGroups" uniqueID="${product.id}"/>
			<@s.submit key="label.cancel" onclick="return redirect( '${cancelUrl}' );"/>
			<@s.submit key="label.save" />
		</div>
	</@s.form>
	</div>

<#if sessionUser.hasAccess("tag") >
	<div class="columnSeperator" >
		<@s.text name="label.or"/>
	</div>
	
	<div id="addComponents" class="componentTypes" >
		<div class="inspectionHeader">
			<h3><@s.text name="label.add_new_component"/></h3>
			<p class="instructions smallInstructions">
				<@s.text name="instructions.add_new_component"/>
			</p>
		</div>
		
		<#list subTypes?sort_by('name') as type >
			<div id="subProductType_${type.id}" class="component"> 
				<div class="definition">
					<div class="identifier">${type.name}</div> 
					<div class="createOptions">
						<a href="<@s.url action="productAdd" namespace="/ajax"  productTypeId="${type.id}" token="${token}"/>" onclick="addSubProduct(${type.id}, ${(product.owner.id)}); return false"><@s.text name="label.add_new" /></a> | 
						<a href='<@s.url action="products" namespace="/ajax"  productTypeId="${type.id}"/>'  class='lightview' rel='ajax' title='<@s.text name="title.productlookup"/> :: :: scrolling:true, width: 700, height: 420, ajax: { onComplete: findSubProduct }' >
				  			<@s.text name="label.find_existing" />
				  		</a>
				  	</div>
			  	</div>
			</div>
		</#list>
	</div>
</#if>



