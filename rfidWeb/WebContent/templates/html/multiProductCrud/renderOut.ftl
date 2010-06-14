<head>
	<@n4.includeStyle href="loadingPage" type="page"/>
</head>

	<@s.form id="performMultiInspect" action="selectEventType" namespace="/multiInspect">
	<#list listOfIds as assetId>
			<@s.hidden name="assetIds[${assetId_index}]" value="${assetId}"/> 
	</#list>

	<@n4.includeScript>
		onDocumentLoad(function() {
				document.getElementsByTagName('h1')[0].hide();			
				$('performMultiInspect').submit();
		});
	</@n4.includeScript>
</@s.form>
<div class="centerWheel">
	<img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/>
	
</div>
<div class="loadingText">
	<h1>
		<@s.text name="label.creating_multi_inspect_products" />
	</h1>
</div>