<div id="subAsset_${subAsset.id}" class="subComponent done">
	<div class="definition">
		<div class="identifier">
			<span>${subAsset.type.name!}</span> | <label>${action.getNameFor(subAsset)!action.getText("label.not_labeled")}</label>
		</div>
	</div>
	
	<div class="performedEvent" id="eventPreformed_${subAsset.id}" >
		<ul>
			<#if masterEvent?exists >
				<#list action.getEventsFor(subAsset) as subEvent >
					<li>${subEvent.type.name!} - <a href="<@s.url action="subEventEdit" uniqueID="${subEvent.id}" token="${token}"/>"><@s.text name="label.edit_this_event"/></a></li>
				</#list>
			</#if>
		</ul>		
	</div>
</div>