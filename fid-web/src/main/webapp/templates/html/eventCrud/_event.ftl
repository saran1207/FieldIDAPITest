
<#if !eventFormHelper.getAvailableSections( formEvent ).isEmpty() >
	<script type="text/javascript">
		sectionRotator = new SectionRotator(  ${eventFormHelper.getAvailableSections( formEvent )?size}, 0, '${identifier}' );
	</script>
	<div id="eventForm_${identifier}" class="eventForm">
			
		<h2>
			<span id="formTitle" class="formTitle"><@s.text name="label.eventform"/></span>
			<span class="jumpto" id="jumpToSection">
				<img src="<@s.url value="/images/jump-to-icon.png"/>" />
				<@s.text name="label.jumpto"/>
                &nbsp;
                <@s.select name="jumpToSections" id="jumpTo_${identifier}" headerKey="" headerValue="" list="eventFormHelper.currentCriteriaSections" listKey="id" listValue="shortName" theme="simple"/>
			</span>
            <div style="clear:both;width:0;height:0;"></div>
		</h2>
		
		<#if form_action="EDIT" && !formEvent.editable>
			<p class="instruction" style="text-align: center;" ><@s.text name="label.uneditableevent"/></p>
		</#if>
		
		<#assign criteriaCount=0/>
        <#assign criteriaSkipped=0/>
		<#list eventFormHelper.getAvailableSections( formEvent ) as section >
			<div id="section_${identifier}_${section_index}" <#if section_index != 0 >style="display:none"</#if> >
				<h3>
					<span class="eventSectionTitle">${section.title}</span> 
					<span class="eventSectionIndex">
						<label>${section_index +1} <@s.text name="label.of"/> ${eventFormHelper.getAvailableSections( formEvent )?size} <@s.text name="label.sections"/></label>
						<a href="javascript:void(0);" id="downIndex_${identifier}_${section_index}_top" selectedIndex="${section_index-1}"><img src="<@s.url value="/images/prev-button.png"/>" alt="&lt; "/></a>
						<a href="javascript:void(0);" id="upIndex_${identifier}_${section_index}_top" selectedIndex="${section_index+1}"><img src="<@s.url value="/images/next-button.png"/>" alt="&gt; "/></a>
					</span>
				</h3>
				<script type="text/javascript">
					$( 'downIndex_${identifier}_${section_index}_top' ).observe( 'click', jumpLinkToSection );
					$( 'downIndex_${identifier}_${section_index}_top' ).sectionRotator = sectionRotator;
					$( 'upIndex_${identifier}_${section_index}_top' ).observe( 'click', jumpLinkToSection );
					$( 'upIndex_${identifier}_${section_index}_top' ).sectionRotator = sectionRotator;
				</script>
				
				<#if form_action="ADD">
					<#include "_eventFormEdit.ftl"/>
				<#elseif form_action="EDIT" && formEvent.editable>
					<#include "_eventFormEdit.ftl"/>
				<#else>
					<#include "_eventForm.ftl"/>
				</#if>
				<h3>
					<span class="eventSectionIndex">
						<label>${section_index +1} <@s.text name="label.of"/> ${eventFormHelper.getAvailableSections( formEvent )?size} <@s.text name="label.sections"/></label>
						<a href="javascript:void(0);" id="downIndex_${identifier}_${section_index}" selectedIndex="${section_index-1}"><img src="<@s.url value="/images/prev-button.png"/>" alt="&lt; "/></a>
						<a href="javascript:void(0);" id="upIndex_${identifier}_${section_index}" selectedIndex="${section_index+1}"><img src="<@s.url value="/images/next-button.png"/>" alt="&gt; "/></a>
					</span>
				</h3>
				<script type="text/javascript">
					$( 'downIndex_${identifier}_${section_index}' ).observe( 'click', jumpLinkToSection );
					$( 'downIndex_${identifier}_${section_index}' ).sectionRotator = sectionRotator;
					$( 'upIndex_${identifier}_${section_index}' ).observe( 'click', jumpLinkToSection );
					$( 'upIndex_${identifier}_${section_index}' ).sectionRotator = sectionRotator;
				</script>
			</div>
		</#list>
	</div>
	<script type="text/javascript">
		$( 'jumpTo_${identifier}' ).observe( 'change', jumpSelectToSection );
		$( 'jumpTo_${identifier}' ).sectionRotator = sectionRotator;
	</script>
</#if>
