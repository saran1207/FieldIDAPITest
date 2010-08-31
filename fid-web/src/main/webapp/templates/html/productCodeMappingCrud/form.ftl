<head>
	<script language="javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"> </script>
	<script language="javascript" src="<@s.url value="/javascript/combobox.js"/>"> </script>
	<script type="text/javascript">
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
	</script>
</head>
<#if productCodeMapping.uniqueID?exists>
	${action.setPageType('product_code_mapping','edit')!}
<#else>
	${action.setPageType('product_code_mapping','add')!}
</#if>
<@s.form action="productCodeMappingEdit!save" cssClass="fullForm" theme="fieldid" >
	<@s.hidden name="uniqueID"/>
	<div class="fluidSets">
		<div class="infoSet">
			<label class="label" for="productCode"><@s.text name="label.productcode"/></label>
			<@s.textfield  name="productCode"/>
		</div>
		<div class="infoSet">
			<label class="label" for="customerRefNumber"><@s.text name="label.referencenumber"/></label>
			<@s.textfield name="customerRefNumber" />
		</div>
		<div class="infoSet">
			<input type="hidden" name="productTypeUpdate" value="false" id="productTypeUpdate" />
			<label class="label" for="productType"><@s.text name="label.producttype"/></label>
			<@s.select id="productType" name="productType" emptyOption="true" onchange="updateProductType(this)">
				<#include "/templates/html/common/_productTypeOptions.ftl"/>
			</@s.select>
		</div>	
		
		<div id="infoOptions">
			<#if productTypeBean?exists >
				<#assign fieldPrefix='product' />
				<#assign prefix='product'/>
				<#assign requires=false>
				<#include "/templates/html/productCrud/_attributes.ftl" />
			</#if>
		</div>
	</div>
	<div class="actions">
		<@s.submit key="hbutton.save"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="productCodeMappingList"/>"><@s.text name="label.cancel" /></a>
	</div>
</@s.form >
<script type="text/javascript">
	function updateProductType( productTypeSelect ) {
		productTypeSelect.form.productTypeUpdate.value = "true";
		productTypeSelect.form.action = '<@s.url action="productCodeMappingEdit" includeParams="none"/>';
		productTypeSelect.form.submit();
		productTypeSelect.form.productTypeUpdate.value = "false";
		productTypeSelect.form.action = '<@s.url action="productCodeMappingEdit!save" includeParams="none"/>';
	}
</script>