<#if productCodeMapping.uniqueID?exists>
	${action.setPageType('product_code_mapping','edit')!}
<#else>
	${action.setPageType('product_code_mapping','add')!}
</#if>
<@s.form action="productCodeMappingEdit!save" cssClass="inputForm" theme="css_xhtml" >
	<@s.hidden name="uniqueID"/>
	<div class="formRowHolder">
		<@s.textfield key="label.productcode" name="productCode" labelposition="left"/>
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.referencenumber" name="customerRefNumber" labelposition="left"/>
	</div>
	<div class="formRowHolder">
		<input type="hidden" name="productTypeUpdate" value="false" id="productTypeUpdate" />
		<@s.select id="productType" key="label.producttype" labelposition="left" name="productType" emptyOption="true" onchange="updateProductType(this)">
			<#include "/templates/html/common/_productTypeOptions.ftl"/>
		</@s.select>
	</div>	
	
	<div id="infoOptions">
		<#if productTypeBean?exists >
			<#assign fieldPrefix='product' />
			<#assign prefix='product'/>
			<#include "/templates/html/common/_dynamicOptions.ftl" />
		</#if>
	</div>
	
	<div class="formAction">
		<button  onclick="window.location = ( '<@s.url action="productCodeMappingList" includeParams="none"/>'); return false;" ><@s.text name="hbutton.cancel" /></button>
		<@s.submit key="hbutton.save"/>
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