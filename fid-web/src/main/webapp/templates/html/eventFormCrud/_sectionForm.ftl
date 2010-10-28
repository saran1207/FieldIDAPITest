<#if criteriaSections[section_index]?exists>
	<#assign addCriteriaButton>
		<div class="addCriteriaButton" >
			<button onclick="addCriteria( ${section_index} ); changeToForm(); return false;" ><@s.text name="label.addcriteria"/></button>
		</div>
	</#assign>
	<div id="criteriaSection_${section_index}" 
		<#if (criteriaSections[section_index].retired)?exists && criteriaSections[section_index].retired >class="retired hidden"</#if> >
		<div class="criteriaSectionHead" >
			<span class="criteriaSectionTitle" >
				<a href="javascript:void(0);" id="expandSection_${section_index}" onclick="openSection('sectionContainer_${section_index}', 'expandSection_${section_index}', 'collapseSection_${section_index}');return false"><img src="<@s.url value="/images/expandLarge.gif" includeParams="none"/>" /></a>
				<a href="javascript:void(0);" id="collapseSection_${section_index}" onclick="closeSection('sectionContainer_${section_index}', 'collapseSection_${section_index}', 'expandSection_${section_index}');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif" includeParams="none"/>" /></a>				 
				<@s.text name="label.sectionname"/> <@s.textfield name="criteriaSections[${section_index}].title" theme="simple"/> 
				
				<@s.hidden id="id_${section_index}" name="hiddenid" value="${(criteriaSections[section_index].id)!0}"/>
				<@s.hidden id="retire_${section_index}" name="criteriaSections[${section_index}].retired" theme="simple"/>
				<a href="javascript:void(0);" onclick="retireSection(${section_index}); changeToForm(); return false;"><img src="<@s.url value="/images/retire.gif" />" /></a>
			</span>
			${addCriteriaButton}
		</div>
		<div id="sectionContainer_${section_index}" style="display:none; overflow:hidden" >
			<div class="criteriaGroup" id="section_${section_index}" >
				<#if (section.criteria)?exists >
					<#list section.criteria as criteriaList >
						<#include "_criteriaForm.ftl"/>
					</#list>
				</#if>
			</div>
			
			${addCriteriaButton}
		</div>
		
	</div>
</#if>