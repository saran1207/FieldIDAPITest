<ul >
	<li  class="listTitle"><@s.text name="label.event_types"/>
		<ul>
			<#if !action.getInspectionTypesFor(current_type.id).empty>
				<#list action.getInspectionTypesFor(current_type.id) as inspectionType>
					<li class="listValues">|- ${inspectionType.name?html}</li>
				</#list>
			<#else>	
				<li class="listValues"><@s.text name="label.no_event_types_connected"/></li>
			</#if>
		</ul>
	</li>
</ul>			
