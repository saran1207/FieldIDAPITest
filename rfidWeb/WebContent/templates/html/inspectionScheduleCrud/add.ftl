${action.setPageType('product', 'add')!}
<#include "_list.ftl"/>

<div class="formAction">
	<@s.url action="productAdd" id="addProductUrl"/>
	<@s.submit key="label.imdone" onclick="redirect('${addProductUrl}');"/>
</div>