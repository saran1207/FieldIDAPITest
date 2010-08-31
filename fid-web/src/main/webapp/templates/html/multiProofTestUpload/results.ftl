${action.setPageType('inspection', 'multi_proof_test')!}
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
						<#list inspectionProcessingFailureMap[key].keySet() as serialnumber >
							<p>
								<label><@s.text name="label.serialnumber"/>: ${serialnumber}</label>
								<span>
									<#if inspectionProcessingFailureMap[key][serialnumber]?exists>
										<a href='<@s.url action="inspection" namespace="/aHtml/iframe"  productId="${inspectionProcessingFailureMap[key][serialnumber].product.uniqueID}" uniqueID="${inspectionProcessingFailureMap[key][serialnumber].id}"/>'  class='lightview' rel='iframe' title='<@s.text name="title.viewinspection"/> :: :: scrolling:true, width: 520, height: 420' >
							  				<@s.text name="link.view" />
							  			</a>
									<#else>
										<@s.text name="label.could_not_upload_prooftest"/>
											
												
									</#if>
								</span>
							</p>
						</#list>
						<#if inspectionProcessingFailureMap[key].keySet().isEmpty() >
							<@s.text name="label.failedtoprocess"/>
						</#if>		
					</#if>
				</td>
			</tr>
		</#list>
</table>


