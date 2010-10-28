<div id="subAsset_${subAsset.id}" class="subComponent done">
	<div class="definition">
		<div class="identifier">
			<span>${subAsset.type.name!}</span> | <label>${action.getNameFor(subAsset)!action.getText("label.not_labeled")}</label>
		</div>
	</div>
	
	<div class="performedInspection" id="inspectionPreformed_${subAsset.id}" >
		<ul>
			<#if masterInspection?exists >
				<#list action.getInspectionsFor(subAsset) as subInspection >
					<li>${subInspection.type.name!} - <a href="<@s.url action="subEventEdit" uniqueID="${subInspection.id}" token="${token}"/>"><@s.text name="label.edit_this_event"/></a></li>
				</#list>
			</#if>
		</ul>		
	</div>
</div>