<h2>1. <@s.text name="label.enter_asset_attributes"/></h2>
<div class="stepContent" id="step1">
	<div id="form1required" style="display: none;" class="errorMessage">
		<@s.text name="error.attributesrequired"/>
	</div>
	<#include "/templates/html/common/_formErrors.ftl"/>
	<#include "/templates/html/productCrud/_productConfigForm.ftl"/>
			
	<div class="stepAction">
		<@s.submit theme="fieldidSimple" key="label.continue" onclick="validateForm1(); return false;"/>
	</div>
</div>

<script type="text/javascript">
	updateProductType($('productType'));
</script>