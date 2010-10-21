<#assign updateSubProductAction="updateSubProductInInspection"/>
<#assign uniqueID=assetId/>
<#assign inInspection=true />
<div id="subProduct_${subProduct.asset.id}" class="subComponent <#if !action.getInspectionsFor(subProduct.asset).empty>done</#if>">
	<div id="subProductHeader_${subProduct.asset.id}">
		<#include "../subProductCrud/_header.ftl"/>
	</div>
	<div class="performedInspection" id="inspectionPreformed_${subProduct.asset.id}" >
		<ul>
			<#if masterInspection?exists >
				<#list action.getInspectionsFor(subProduct.asset) as subInspection >
					<li>${subInspection.type.name!} - <a href="<@s.url action="subInspectionAdd" uniqueID="${subInspection.id}" assetId="${subProduct.asset.id}" type="${subInspection.type.id}" parentAssetId="${assetId}" token="${token}"/>"><@s.text name="label.edit_this_event"/></a></li>
				</#list>
			</#if>
			<#if action.getInspectionsFor(subProduct.asset).empty>
				<li><@s.text name="label.no_events_have_been_completed"/></li> 
			</#if>
		</ul>		
	</div>
</div>

