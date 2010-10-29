${action.setPageType('asset', 'list')!}

<#if !inVendorContext>
	<#assign actionTarget="asset"/>
<#else>
	<#assign actionTarget="assetTraceability"/>
</#if>

<#include "../eventGroup/_assetList.ftl"/>


<script type="text/javascript">
$$('.assetLink').each(function(element) {
		element.observe('click', goToAssetListener);
	});
function goToAssetListener(event) {
	<#if !inVendorContext>
		var url = '<@s.url action="asset" />' + "?uniqueID=" + Event.element( event ).getAttribute( 'assetId' );
	<#else>
		var url = '<@s.url action="assetTraceability" useContext="true"/>' + "&uniqueID=" + Event.element( event ).getAttribute( 'assetId' );
	</#if>
	
	
	event.stop();
	redirect(url);
}
				
				
</script>