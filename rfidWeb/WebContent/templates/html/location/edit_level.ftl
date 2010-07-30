${action.setPageType('predefined_locations', 'level_list')!}


	<#assign nodeIndex = action.getNodeLevel(uniqueID)-1 >


<#if !action.getLevel(nodeIndex)??>
	<h2 ><@s.text name="label.add_level"/></h2>
	<@s.form action="predefinedLocationLevelCreate" theme="fieldid" cssClass="fullForm fluidSets pageSection">
		<@s.hidden name="levelName.index" value="${nodeIndex}"/>
		<div class="infoSet">
			<label class="label"><@s.text name="label.name"/></label>
			<@s.textfield name="levelName.name"/>
		</div>
		<div class="blockSeparated">
			<@s.submit key="label.add"/>
		</div>
	</@s.form>
<#else>
	<h2><@s.text name="label.edit_a_level"/></h2>
	<@s.form action="predefinedLocationLevelUpdate" theme="fieldid" cssClass="fullForm fluidSets pageSection">
		<@s.hidden name="levelName.index" value="${nodeIndex}"/>
		<div class="infoSet">
			<label class="label"><@s.text name="label.name"/></label>
			<@s.textfield name="levelName.name" value="${action.getLevel(nodeIndex).name}"/>
		</div>
		<div >
			<@s.submit key="label.edit"/>
		</div>
	</@s.form>
	<@s.form action="predefinedLocationLevelDelete" theme="fieldid" cssClass="fullForm fluidSets pageSection">
		<@s.hidden name="levelName.index" value="${nodeIndex}"/>
		<div >
			<@s.submit key="label.remove"/>
		</div>
	</@s.form>
</#if>	


