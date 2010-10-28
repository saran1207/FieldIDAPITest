<ul >
	<li  class="listTitle"><@s.text name="label.inspection_types"/>
		<ul>
			<#if !action.getInspectionTypesFor(current_type.id).empty>
				<#list action.getInspectionTypesFor(current_type.id) as inspectionType>
					<li class="listValues">|- ${inspectionType.name?html}</li>
				</#list>
			<#else>	
				<li class="listValues"><@s.text name="label.no_inspection_types_connected"/></li>
			</#if>
		</ul>
	</li>
</ul>			