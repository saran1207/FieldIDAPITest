
<#if !inspectionFormHelper.getAvailableSections( formInspection ).isEmpty() >
	<script type="text/javascript">
		sectionRotator = new SectionRotator(  ${inspectionFormHelper.getAvailableSections( formInspection )?size}, 0, '${identifier}' );
	</script>
	<div id="inspectionForm_${identifier}" class="inspectionForm">
			
		<h2>
			<span id="formTitle"><@s.text name="label.eventform"/></span> 
			<span id="jumpToSection"><@s.text name="label.jumpto"/>: <@s.select name="jumpToSections" id="jumpTo_${identifier}" headerKey="" headerValue="" list="inspectionFormHelper.currentCriteriaSections" listKey="id" listValue="name" theme="simple"/></span>
		</h2>
		
		<#if form_action="EDIT" && !formInspection.editable>
			<p class="instruction" style="text-align: center;" ><@s.text name="label.uneditableevent"/></p>
		</#if>
		
		<#assign criteriaCount=0/>
		<#list inspectionFormHelper.getAvailableSections( formInspection ) as section >
			<div id="section_${identifier}_${section_index}" <#if section_index != 0 >style="display:none"</#if> >
				<h3>
					<span class="youAreOn"><@s.text name="label.youareon"/>: </span><span class="inspectionSectionTitle">${section.title}</span> 
					<span class="inspectionSectionIndex"><a href="javascript:void(0);" id="downIndex_${identifier}_${section_index}" selectedIndex="${section_index-1}"><img  width="17" src="<@s.url value="/images/nav_blue_left.png"/>" alt="&lt; "/></a> [ ${section_index +1} / ${inspectionFormHelper.getAvailableSections( formInspection )?size} ] <a href="javascript:void(0);" id="upIndex_${identifier}_${section_index}" selectedIndex="${section_index+1}"><img width="17" src="<@s.url value="/images/nav_blue_right.png"/>" alt="&gt; "/></a></span>
				</h3>
				<script type="text/javascript">
					$( 'downIndex_${identifier}_${section_index}' ).observe( 'click', jumpLinkToSection );
					$( 'downIndex_${identifier}_${section_index}' ).sectionRotator = sectionRotator;
					$( 'upIndex_${identifier}_${section_index}' ).observe( 'click', jumpLinkToSection );
					$( 'upIndex_${identifier}_${section_index}' ).sectionRotator = sectionRotator;
				</script>
				
				<#if form_action="ADD">
					<#include "_eventFormEdit.ftl"/>
				<#elseif form_action="EDIT" && formInspection.editable>
					<#include "_eventFormEdit.ftl"/>
				<#else>
					<#include "_eventForm.ftl"/>
				</#if>
				
			</div>
		</#list>
	</div>
	<script type="text/javascript">
		$( 'jumpTo_${identifier}' ).observe( 'change', jumpSelectToSection );
		$( 'jumpTo_${identifier}' ).sectionRotator = sectionRotator;
	</script>
</#if>
