<head>
	<@n4.includeStyle type="page" href="event" />
</head>
${action.setPageType('event', 'select_asset')!}

<#include "_eventSearchForm.ftl"/>


<#assign actionTarget="eventGroups"/>
<#include "_assetList.ftl"/>
<script type="text/javascript">
	$$('.assetLink').each(function(element) {
			element.observe('click', goToAssetListener);
		});
	function goToAssetListener(event) {
		var url = '<@s.url action="eventGroups"/>';
		
		event.stop();
		redirect( url + "?uniqueID=" + Event.element( event ).getAttribute( 'assetId' ) );
	}
</script>