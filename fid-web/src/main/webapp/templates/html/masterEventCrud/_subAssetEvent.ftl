<#assign updateSubAssetAction="updateSubAssetInEvent"/>
<#assign uniqueID=assetId/>
<#assign inEvent=true />
<div id="subAsset_${subAsset.asset.id}" class="subComponent <#if !action.getEventsFor(subAsset.asset).empty>done</#if>">
	<div id="subAssetHeader_${subAsset.asset.id}">
		<#include "../subAssetCrud/_header.ftl"/>
	</div>
	<div class="performedEvent" id="eventPreformed_${subAsset.asset.id}" >
		<ul>
			<#if masterEvent?exists >
				<#list action.getEventsFor(subAsset.asset) as subEvent >
					<li>${subEvent.type.name!} - <a href="<@s.url action="subEventAdd" uniqueID="${subEvent.id}" assetId="${subAsset.asset.id}" type="${subEvent.type.id}" parentAssetId="${assetId}" token="${token}"/>"><@s.text name="label.edit_this_event"/></a></li>
				</#list>
			</#if>
			<#if action.getEventsFor(subAsset.asset).empty>
				<li><@s.text name="label.no_events_have_been_completed"/></li> 
			</#if>
		</ul>		
	</div>
</div>

