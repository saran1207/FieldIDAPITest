<#if !namespace?exists><#assign namespace="/"/></#if>
<div id="${productSearchDiagram!'productSearch'}">
	<@s.form method="get" id="${productFormId!'productSearchForm'}" cssClass="simpleInputForm" action="${productSearchAction}" namespace="${namespace}" theme="simple">
		<label class="label"><@s.text name="label.smartsearch"/></label>
		<span><@s.textfield name="search" /></span>
		<#if useOverRides?exists && useOverRides >
			<@s.hidden name="productTypeId">
				<#if overRideProductType?exists>
				 	<@s.param name="value">${overRideProductType}</@s.param>
				<#elseif product?exists && !product.new >
					<@s.param name="value"></@s.param>
				</#if>
			</@s.hidden>
			<@s.hidden name="excludeId"/>
		</#if>
		<@s.submit name="load" key="hbutton.load"/>
	</@s.form>
</div>