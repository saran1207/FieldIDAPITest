${action.setPageType('product', 'list')!}

<#assign actionTarget="product"/>
<#include "../inspectionGroup/_productList.ftl"/>


<script type="text/javascript">
$$('.productLink').each(function(element) {
		element.observe('click', goToAssetListener);
	});
function goToAssetListener(event) {
	var url = '<@s.url action="product"/>';
	
	event.stop();
	redirect( url + "?uniqueID=" + Event.element( event ).getAttribute( 'productId' ) );
}
				
				
</script>