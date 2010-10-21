<h2>2. <@s.text name="label.set_quantity"/></h2>
<div class="stepContent" id="step2" style="display:none">
	<p>${action.getTextArg("label.how_many_assets", maxAssets)}</p>
	
	<div class="formRowHolder">
		<label for="quantity"><@s.text name="label.quantity"/>: </label>
		<@s.textfield id="quantity" name="quantity" labelposition="left" value="2" onkeyup="quantityIsValid();"/>
		<div id="quantityInvalid" style="display: none;" class="errorMessage">${action.getTextArg("error.invalid_quantity", maxAssets)}</div>
	</div>
	
	<div class="stepAction">
		<@s.submit id="step2next" key="label.continue" onclick="validateForm2(); return false;"/>
		<@s.text name="label.or"/> <a href="#step1" onclick="backToStep1(); return false;"><@s.text name="label.back_to_step"/> 1</a>
		
	</div>
</div>

<script type="text/javascript">
	maxAssets = ${maxAssets};
</script>