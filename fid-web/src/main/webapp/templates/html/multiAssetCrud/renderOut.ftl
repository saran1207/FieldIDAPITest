${action.setPageType('asset', 'multi_add')!}

<head>
	<@n4.includeStyle href="loadingPage" type="page"/>
</head>

	<@s.form id="performMultiEvent" action="selectEventType" namespace="/multiEvent">
	<#list listOfIds as assetId>
			<@s.hidden name="assetIds[${assetId_index}]" value="${assetId}"/> 
	</#list>

	<@n4.includeScript>
		onDocumentLoad(function() {
				$('performMultiEvent').submit();
		});
	</@n4.includeScript>
</@s.form>
<div class="centerWheel">
	<img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/>
	
</div>
<div class="loadingText">
	<h1>
		<@s.text name="label.creating_multi_event_assets" />
	</h1>
</div>