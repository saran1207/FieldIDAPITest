${action.setPageType('asset', 'list')!}

<#if !namespace?exists><#assign namespace="/"/></#if>
<div id="results">

<#if page.validPage() && page.hasResults()  >	
	<#assign currentAction="assetInformation.action" />
	<#include '../common/_pagination.ftl' />
	<h2 class="clean"><@s.text name="label.found_multiple_assets"/></h2>
	<div id="resultsTable">
		<table class="list">
			<tr>
				<th>&nbsp;</th>
				<th><@s.text name="${Session.sessionUser.serialNumberLabel}"/></th>
				<th><@s.text name="label.rfidnumber"/></th>
				<th><@s.text name="label.owner"/></th>
				<th><@s.text name="label.assettype"/></th>
				<th><@s.text name="label.identified"/></th>
				<th><@s.text name="label.reference_number"/></th>
			</tr>
			<#list page.list as asset>
				<tr>
					<td class="selectAction"><button class="assetLink" assetId="${asset.id}"><@s.text name="label.select"/></button></td>
					<td>${asset.serialNumber?html}</td>
					<td>${(asset.rfidNumber?html)!}</td>
					<td>${(asset.owner.name?html)!}</td>
					<td>${asset.type.name?html}</td>
					<td>${action.formatDate(asset.identified, false)}</td>
					<td>${(asset.customerRefNumber?html)!}</td>
				</tr>
			</#list>
		</table>
		<#include '../common/_pagination.ftl' />
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<@s.text name="message.emptyassetlist" />
		</p>
	</div>
</#if>

</div>



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