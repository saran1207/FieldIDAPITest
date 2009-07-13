<ul class="options <#if navOptions.filteredOptions.empty> emptyOptions</#if>">
	<#list navOptions.filteredOptions as option>
		<#if option.type != 'entity' || navOptions.entityLoaded() >
			<#if option.conditionalView?eval >
				<li class="<#if option.type == "add">add</#if> <#if option.name == navOptions.currentAction>selected</#if>">
					<#if option.name != navOptions.currentAction>
						<@s.url id="url" action="${option.action}">
							<#list option.urlParams?keys as param>
								<@s.param name="${param}">${("("+option.urlParams[param]+")!")?eval}</@s.param>
							</#list>
						</@s.url>
						<a href="${url}">		
					</#if>
					<@s.text name="${option.label}"/>
					<#if option.name != navOptions.currentAction>
						</a>		
					</#if>
				</li>
			</#if>
		</#if>
	</#list>
</ul>