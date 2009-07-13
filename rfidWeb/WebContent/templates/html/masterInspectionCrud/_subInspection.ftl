<div id="subProduct_${subProduct.id}" class="subComponent done">
	<div class="definition">
		<div class="identifier">
			<span>${subProduct.type.name!}</span> | <label>${action.getNameFor(subProduct)!action.getText("label.not_labeled")}</label>
		</div>
	</div>
	
	<div class="performedInspection" id="inspectionPreformed_${subProduct.id}" >
		<ul>
			<#if masterInspection?exists >
				<#list action.getInspectionsFor(subProduct) as subInspection >
					<li>${subInspection.type.name!} - <a href="<@s.url action="subInspectionEdit" uniqueID="${subInspection.id}" token="${token}"/>"><@s.text name="label.edit_this_event"/></a></li>
				</#list>
			</#if>
		</ul>		
	</div>
</div>