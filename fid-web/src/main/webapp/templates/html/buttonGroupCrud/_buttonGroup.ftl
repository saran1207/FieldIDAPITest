<ul class="buttonGroup">
	<#list buttons as button >
		<#if !button.retired >
			<li>
				<div class="eventButton"><img src="<@s.url value="/images/eventButtons/${button.buttonName}.png"/>"/></div>
				<div>${button.displayName}</div>
			</li>
		</#if>
	</#list>
</ul>