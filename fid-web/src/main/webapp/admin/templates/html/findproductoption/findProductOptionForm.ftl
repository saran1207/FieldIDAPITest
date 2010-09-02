
<@s.form action="findProductOptionCrud!save" namespace="/admin" method="post" theme="xhtml">
	<@s.hidden name="id" value="%{id}" />
	<@s.label name="findProductOption.findProductOption.description" value="%{findProductOption.findProductOption.description}" label="Description: " />
	<@s.label name="findProductOption.findProductOption.value" value="%{findProductOption.findProductOption.value}" label="Value: " />
	<@s.textfield name="findProductOption.weight" value="%{findProductOption.weight}" label="Weight: " />
	<@s.textfield name="findProductOption.mobileWeight" value="%{findProductOption.mobileWeight}" label="Mobile Weight: " />	
	<@s.submit />
</@s.form>