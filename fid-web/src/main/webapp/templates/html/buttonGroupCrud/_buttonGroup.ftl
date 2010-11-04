<ul class="buttonGroup">
	<#list states as state >
		<#if !state.retired >
			<li>
				<div class="eventButton"><img src="<@s.url value="/images/eventButtons/${state.buttonName}.png"/>"/></div> 
				<div>${state.displayName}</div>
			</li>
		</#if>
	</#list>
</ul>