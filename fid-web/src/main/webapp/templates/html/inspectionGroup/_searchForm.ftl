<#if !namespace?exists><#assign namespace="/"/></#if>
<div id="${assetSearchDiagram!'assetSearch'}">
	<@s.form method="get" id="${assetFormId!'assetSearchForm'}" cssClass="simpleInputForm" action="${assetSearchAction}" namespace="${namespace}" theme="simple">
		<label class="label"><@s.text name="label.smartsearch"/></label>
		<span><@s.textfield name="search" /></span>
		<#if useOverRides?exists && useOverRides >
			<@s.hidden name="assetTypeId">
				<#if overRideAssetType?exists>
				 	<@s.param name="value">${overRideAssetType}</@s.param>
				<#elseif asset?exists && !asset.new >
					<@s.param name="value"></@s.param>
				</#if>
			</@s.hidden>
			<@s.hidden name="excludeId"/>
		</#if>
		<@s.submit name="load" key="hbutton.load"/>
	</@s.form>
</div>