


<#list publishedAssetTypes as assetType>
	<ul class="catalogPackage">
		<li>
			<@s.checkbox name="importAssetTypeIds['${assetType.id}']" /> <span class="catalogItem">${assetType.name?html}</span>
			<a href="#openDetails;" onclick="$('details_${assetType.id}').toggle(); $('detail_${assetType.id}').toggle(); $('closeDetail_${assetType.id}').toggle(); return false;"><span id="detail_${assetType.id}"><@s.text name="label.details"/></span><span id="closeDetail_${assetType.id}" style="display:none"><@s.text name="label.close_details"/></span></a>
			<div id="details_${assetType.id}" style="display:none" class="main">
				<#if !action.getSubTypesFor(assetType.id).empty>
					<ul>	
						<li  class="listTitle">|- <@s.text name="label.sub_types"/>
						<ul >
						<#list action.getSubTypesFor(assetType.id) as subType>
							<li  class="listValues">${subType.name?html}
								<#assign current_type=subType/>
								<#include "_inspectionTypes.ftl"/>
							</li>
						</#list>
						</ul>
					</li>
					</ul>
				</#if>
				<#assign current_type=assetType/>
				<#include "_inspectionTypes.ftl"/>
			</div>
		</li>
	</ul>

</#list>