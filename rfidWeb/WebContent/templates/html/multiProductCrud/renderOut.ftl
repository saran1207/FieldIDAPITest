<head>
	<@s.form id="performMultiInspect" action="selectEventType" namespace="/multiInspect">
		<#list listOfIds as assetId>
				<@s.hidden name="assetIds[${assetId_index}]" value="${assetId}"/> 
		</#list>
		<@n4.includeScript>
			onDocumentLoad(function() {
								
					$('performMultiInspect').submit();
						
			});
		</@n4.includeScript>
	</@s.form>
</head>
<div class="centerWheel"
	<img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/>
</div>