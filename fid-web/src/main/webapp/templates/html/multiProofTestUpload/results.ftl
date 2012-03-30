${action.setPageType('event', 'multi_proof_test')!}

<head>
    jQuery(document).ready(function(){
        jQuery('#exportLink').colorbox( { title : '<@s.text name="title.viewevent"/>, iframe: true, scrolling: true, width: '520px', height: '420px'});
    });
</head>
<div class="actions">
	<a href="<@s.url action="multiProofTest.action" />" ><@s.text name="label.uploadmore"/></a>
</div>
<table class="list">
	<tr>
		<th class="rowName"><@s.text name="label.filename"/></th>
		<th><@s.text name="label.result"/></th>
	</tr>
		<#list fileProcessingFailureMap.keySet() as key >
			<tr  >
				<td>${key}</td>
				<td>
					<#if fileProcessingFailureMap[key]?exists >
						<@s.text name="label.failedtoprocess"/>
					<#else>
						<#list eventProcessingFailureMap[key].keySet() as identifier >
							<p>
								<label><@s.text name="label.id_number"/>: ${identifier}</label>
								<span>
									<#if eventProcessingFailureMap[key][identifier]?exists>
										<a href='<@s.url action="event" namespace="/aHtml/iframe"  assetId="${eventProcessingFailureMap[key][identifier].asset.uniqueID}" uniqueID="${eventProcessingFailureMap[key][identifier].id}"/>' >
							  				<@s.text name="link.view" />
							  			</a>
									<#else>
										<@s.text name="label.could_not_upload_prooftest"/>
											
												
									</#if>
								</span>
							</p>
						</#list>
						<#if eventProcessingFailureMap[key].keySet().isEmpty() >
							<@s.text name="label.failedtoprocess"/>
						</#if>		
					</#if>
				</td>
			</tr>
		</#list>
</table>


