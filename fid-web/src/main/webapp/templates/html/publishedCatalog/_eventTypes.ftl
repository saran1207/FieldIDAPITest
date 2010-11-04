<ul >
	<li  class="listTitle"><@s.text name="label.event_types"/>
		<ul>
			<#if !action.getEventTypesFor(current_type.id).empty>
				<#list action.getEventTypesFor(current_type.id) as eventType>
					<li class="listValues">|- ${eventType.name?html}</li>
				</#list>
			<#else>	
				<li class="listValues"><@s.text name="label.no_event_types_connected"/></li>
			</#if>
		</ul>
	</li>
</ul>			
