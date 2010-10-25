<#assign html >
	<div id="productResults">
	<#if assets?exists >
		<#if !assets.isEmpty() >
			<div id="resultsTable">
				<table class="list">
					<tr>
						<th></th>
						<th><@s.text name="${Session.sessionUser.serialNumberLabel}"/></th>
						<th><@s.text name="label.rfidnumber"/></th>
						<th><@s.text name="label.customer"/></th>
						<th><@s.text name="label.producttype"/></th>
						<th><@s.text name="label.customer_reference"/></th>
					</tr>
					<#list assets as asset >
						<tr>
							<td>
								<@s.url id="baseActionUrl" action="marryCustomerOrder" namespace="/ajax" uniqueID="${asset.id}" />
								<a class="assetLink" href="" onclick="marryCustomerOrder('${baseActionUrl}'); return false;" >
									<@s.text name="label.connectorder" />
								</a>
							</td>
							<td>${asset.serialNumber?html}</td>
							<td>${(asset.rfidNumber?html)!}</td>
							<td>${(asset.owner.name?html)!}</td>
							<td>${asset.type.name?html}</td>
							<td>${(asset.customerRefNumber?html)!}</td>
						</tr>
					</#list>
				</table>
			</div>
		<#else>
			<div class="emptyList" >
				<h2><@s.text name="label.noresults"/></h2>
				<p>
					<@s.text name="message.emptyproductlist" />
				</p>
			</div>
		</#if>
	</#if>
	</div>
</#assign>

<#escape x as x?js_string>
	$('productResults').replace( '${html}' );
	$('productResults').highlight();
</#escape>