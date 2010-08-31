<#assign updateSubProductAction="updateSubProductInInspection"/>
<#assign uniqueID=productId/>
<#assign inInspection=true />
<div id="subProduct_${subProduct.product.id}" class="subComponent <#if !action.getInspectionsFor(subProduct.product).empty>done</#if>">
	<div id="subProductHeader_${subProduct.product.id}">
		<#include "../subProductCrud/_header.ftl"/>
	</div>
	<div class="performedInspection" id="inspectionPreformed_${subProduct.product.id}" >
		<ul>
			<#if masterInspection?exists >
				<#list action.getInspectionsFor(subProduct.product) as subInspection >
					<li>${subInspection.type.name!} - <a href="<@s.url action="subInspectionAdd" uniqueID="${subInspection.id}" productId="${subProduct.product.id}" type="${subInspection.type.id}" parentProductId="${productId}" token="${token}"/>"><@s.text name="label.edit_this_event"/></a></li>
				</#list>
			</#if>
			<#if action.getInspectionsFor(subProduct.product).empty>
				<li><@s.text name="label.no_events_have_been_completed"/></li> 
			</#if>
		</ul>		
	</div>
</div>

