${action.setPageType('product', 'add')!}
<#include "list.ftl"/>

<div class="formAction">
	<@s.url action="productAdd" id="addAssetUrl"/>
	<@s.submit key="label.imdone" onclick="redirect('${addAssetUrl}');"/>
</div>