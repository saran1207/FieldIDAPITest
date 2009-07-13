


<#list publishedProductTypes as productType>
	<ul class="catalogPackage">
		<li>
			<@s.checkbox name="importProductTypeIds['${productType.id}']" /> <span class="catalogItem">${productType.name?html}</span> 
			<a href="#openDetails;" onclick="$('details_${productType.id}').toggle(); $('detail_${productType.id}').toggle(); $('closeDetail_${productType.id}').toggle(); return false;"><span id="detail_${productType.id}"><@s.text name="label.details"/></span><span id="closeDetail_${productType.id}" style="display:none"><@s.text name="label.close_details"/></span></a>
			<div id="details_${productType.id}" style="display:none" class="main">
				<#if !action.getSubTypesFor(productType.id).empty>
					<ul>	
						<li  class="listTitle">|- <@s.text name="label.sub_types"/>
						<ul >
						<#list action.getSubTypesFor(productType.id) as subType>
							<li  class="listValues">${subType.name?html}
								<#assign current_type=subType/>
								<#include "_inspectionTypes.ftl"/>
							</li>
						</#list>
						</ul>
					</li>
					</ul>
				</#if>
				<#assign current_type=productType/>
				<#include "_inspectionTypes.ftl"/>
			</div>
		</li>
	</ul>

</#list>