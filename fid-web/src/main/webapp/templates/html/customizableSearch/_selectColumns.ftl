<div class="pageSection">
	<h2>
		<a href="javascript:void(0);" id="open_selectColumnForm" onclick="openSection('selectColumnForm', 'open_selectColumnForm', 'close_selectColumnForm');return false"><img src="<@s.url value="/images/expandLarge.gif" includeParams="none"/>" /></a>
		<a href="javascript:void(0);" id="close_selectColumnForm" onclick="closeSection('selectColumnForm', 'close_selectColumnForm', 'open_selectColumnForm');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif" includeParams="none"/>" /></a>
		<@s.text name="label.selectcolumns"/>
		<span id="selectColumnNotificationArea"></span>
	</h2>
	<div id="selectColumnForm" class="sectionContent" style="display:none;">
		<#list mappingGroups as group>
			<#if !group.staticGroupEmpty>
				<#include "_columnGroup.ftl"/>
			</#if>
		</#list>
	</div>
</div>
