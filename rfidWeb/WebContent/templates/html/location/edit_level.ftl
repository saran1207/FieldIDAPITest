${action.setPageType('predefined_locations', 'level_list')!}

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
		<div class="infoSet">
			<label class="label"><@s.text name="label.name"/></label>
			<@s.textfield name="levelName.name"/>
		</div>
		<div class="actions">
			<@s.submit key="label.add" />
			<@s.text name="label.or"/>
			<a href="<@s.url action="predefinedLocations"/>"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
<#else>
	
	<h2><@s.text name="label.level_name"/></h2>
	<@s.form action="predefinedLocationLevelUpdate" theme="fieldid" cssClass="fullForm fluidSets pageSection">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<@s.hidden name="levelName.index" value="${nodeIndex}"/>
		<div class="infoSet">
			<label class="label"><@s.text name="label.name"/></label>
			<@s.textfield name="levelName.name" value="${action.getLevel(nodeIndex).name}"/>
		</div>
		<div class="actions">
			<@s.submit type="submit" key="hbutton.save" cssClass="saveButton save"/>
		</div>
	</@s.form>
	
	
	<@s.form action="predefinedLocationLevelDelete" theme="fieldid" cssClass="fullForm fluidSets pageSection">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<@s.hidden name="levelName.index" value="${nodeIndex}"/>
		<div >
			<@s.submit key="label.remove"/>
		</div>
		<@s.text name="label.or"/>
		<a href="<@s.url action="predefinedLocations"/>"><@s.text name="label.cancel"/></a>
	</@s.form>
</#if>	


