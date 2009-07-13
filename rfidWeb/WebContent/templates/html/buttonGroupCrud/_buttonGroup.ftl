<ul class="buttonGroup">
	<#list states as state >
		<#if !state.retired >
			<li>
				<div class="inspectionButton"><img src="<@s.url value="/images/inspectionButtons/${state.buttonName}.png"/>"/></div> 
				<div>${state.displayName}</div>
			</li>
		</#if>
	</#list>
</ul>