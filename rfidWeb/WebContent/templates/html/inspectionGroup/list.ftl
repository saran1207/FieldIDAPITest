<head>
	<@n4.includeStyle type="page" href="inspection" />
</head>
${action.setPageType('inspection', 'select_product')!}

<#include "_inspectionSearchForm.ftl"/>


<#assign actionTarget="inspectionGroups"/>
<#include "_productList.ftl"/>
<script type="text/javascript">
	$$('.productLink').each(function(element) {
			element.observe('click', goToAssetListener);
		});
	function goToAssetListener(event) {
		var url = '<@s.url action="inspectionGroups"/>';
		
		event.stop();
		redirect( url + "?uniqueID=" + Event.element( event ).getAttribute( 'productId' ) );
		$('searchResults').update('');
	}
</script>