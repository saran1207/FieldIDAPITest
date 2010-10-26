
<@s.form action="findAssetOptionCrud!save" namespace="/admin" method="post" theme="xhtml">
	<@s.hidden name="id" value="%{id}" />
	<@s.label name="findAssetOption.findAssetOption.description" value="%{findAssetOption.findAssetOption.description}" label="Description: " />
	<@s.label name="findAssetOption.findAssetOption.value" value="%{findAssetOption.findAssetOption.value}" label="Value: " />
	<@s.textfield name="findAssetOption.weight" value="%{findAssetOption.weight}" label="Weight: " />
	<@s.textfield name="findAssetOption.mobileWeight" value="%{findAssetOption.mobileWeight}" label="Mobile Weight: " />
	<@s.submit />
</@s.form>