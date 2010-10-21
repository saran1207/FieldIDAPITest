${action.setPageType('product', 'list')!}

<#if !inVendorContext>
	<#assign actionTarget="product"/>
<#else>
	<#assign actionTarget="productTraceability"/>
</#if>

<#include "../inspectionGroup/_productList.ftl"/>


<script type="text/javascript">
$$('.productLink').each(function(element) {
		element.observe('click', goToAssetListener);
	});
function goToAssetListener(event) {
	<#if !inVendorContext>
		var url = '<@s.url action="product" />' + "?uniqueID=" + Event.element( event ).getAttribute( 'assetId' );
	<#else>
		var url = '<@s.url action="productTraceability" useContext="true"/>' + "&uniqueID=" + Event.element( event ).getAttribute( 'assetId' );
	</#if>
	
	
	event.stop();
	redirect(url);
}
				
				
</script>