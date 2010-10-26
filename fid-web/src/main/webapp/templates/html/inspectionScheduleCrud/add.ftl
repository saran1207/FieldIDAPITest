${action.setPageType('asset', 'add')!}
<#include "list.ftl"/>

<div class="formAction">
	<@s.url action="assetAdd" id="addAssetUrl"/>
	<@s.submit key="label.imdone" onclick="redirect('${addAssetUrl}');"/>
</div>