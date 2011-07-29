<#if !namespace?exists><#assign namespace="/"/></#if>
<div id="results">

<#if assets?exists >
	<#if !assets.isEmpty() >
		<h2 class="clean">
			<#if (assets.size > 1)>
				<@s.text name="label.found_multiple_assets"/>
			<#else>
				&nbsp;
			</#if>
		</h2>
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
				<#list assets as asset >
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
		</div>
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noresults"/></h2>
			<p>
				<@s.text name="message.emptyassetlist" />
			</p>
		</div>
	</#if>
</#if>

</div>
<#if assets?exists >
	<#list assets as asset >
		<@n4.includeScript>
			var asset = null;
			<#assign asset=asset/>
			<#include "/templates/html/assetCrud/_js_asset.ftl"/>
			$$("[assetId='${asset.id}']").first().asset = asset;
		</@n4.includeScript>
	</#list>
</#if>