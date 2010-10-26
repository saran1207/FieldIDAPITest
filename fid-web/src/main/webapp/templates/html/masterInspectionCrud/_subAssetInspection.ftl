<#assign updateSubAssetAction="updateSubAssetInInspection"/>
<#assign uniqueID=assetId/>
<#assign inInspection=true />
<div id="subAsset_${subAsset.asset.id}" class="subComponent <#if !action.getInspectionsFor(subAsset.asset).empty>done</#if>">
	<div id="subAssetHeader_${subAsset.asset.id}">
		<#include "../subAssetCrud/_header.ftl"/>
	</div>
	<div class="performedInspection" id="inspectionPreformed_${subAsset.asset.id}" >
		<ul>
			<#if masterInspection?exists >
				<#list action.getInspectionsFor(subAsset.asset) as subInspection >
					<li>${subInspection.type.name!} - <a href="<@s.url action="subInspectionAdd" uniqueID="${subInspection.id}" assetId="${subAsset.asset.id}" type="${subInspection.type.id}" parentAssetId="${assetId}" token="${token}"/>"><@s.text name="label.edit_this_event"/></a></li>
				</#list>
			</#if>
			<#if action.getInspectionsFor(subAsset.asset).empty>
				<li><@s.text name="label.no_events_have_been_completed"/></li> 
			</#if>
		</ul>		
	</div>
</div>

