${action.setPageType('predefined_locations', 'level_list')!}
<head>
	<@n4.includeStyle type="page" href="location" />
</head>
<#if uniqueID==-1>
	<#assign nodeIndex = 0 >
<#else>
	<#assign nodeIndex = action.getNodeLevel(uniqueID)-1 >
</#if>

<#if !action.getLevel(nodeIndex)??>
	<h2 ><@s.text name="label.add_level"/></h2>
	<@s.form action="predefinedLocationLevelCreate" theme="fieldid" cssClass="fullForm">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<@s.hidden name="levelName.index" value="${nodeIndex}"/>
		<div class="infoSet location">
			<label class="label"><@s.text name="label.name"/></label>
			<@s.textfield name="levelName.name"/>
			
			<div class="formActions buttonGroup">
				<@s.submit key="label.add" />
				<@s.text name="label.or"/>
				<a href="<@s.url action="predefinedLocations"/>"><@s.text name="label.cancel"/></a>
			</div>
		</div>
		
	</@s.form>
<#else>
	<div class="addBox">
		<@s.form action="predefinedLocationLevelUpdate" theme="fieldid" cssClass="fullForm">
			<#include "/templates/html/common/_formErrors.ftl"/>
			<@s.hidden name="levelName.index" value="${nodeIndex}"/>
			<div class="addLocationContent">
				<div class="infoSet location">
					<label class="label"><@s.text name="label.level_name"/></label>
					<@s.textfield name="levelName.name" value="${action.getLevel(nodeIndex).name}"/>
					<div class="formActions buttonGroup">
						<@s.submit type="submit" key="hbutton.save" cssClass="saveButton save"/>
						<@s.text name="label.or"/>
						<a href="javascript:{}" onclick="$('levelDelete').submit()" ><@s.text name="label.remove"/></a>
						<@s.text name="label.or"/>
						<a href="<@s.url action="predefinedLocations"/>"><@s.text name="label.cancel"/></a>
					</div>
				</div>
			</div>
		</@s.form>
			
		<@s.form id="levelDelete" action="predefinedLocationLevelDelete" theme="fieldid" cssClass="fullForm fluidSets pageSection">
			<#include "/templates/html/common/_formErrors.ftl"/>
			<@s.hidden name="levelName.index" value="${nodeIndex}"/>
		</@s.form>
		
	</div>
</#if>	


