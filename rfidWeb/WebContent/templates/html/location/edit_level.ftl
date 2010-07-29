${action.setPageType('predefined_locations', 'level_list')!}
<head>

	<@n4.includeScript>
		lastIndex = ${levels.size() - 1};
		deleteLevelUrl = '<@s.url action="predefinedLocationLevelDelete"/>';
	</@n4.includeScript>
</head>

<@s.hidden id="node" name="nodeId"/>
<@s.hidden name="nodeId" value="${uniqueID}"/> 
<@s.form action="predefinedLocationLevelCreate" theme="fieldid" cssClass="fullForm fluidSets pageSection">
	<h2 ><@s.text name="label.add_level"/></h2>
	<@s.hidden name="levelName.index" value="${levels.size()}"/>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.name"/></label>
		<@s.textfield name="levelName.name"/>
				<label class="label"><@s.text name="${nodeLevel}"/></label>
	</div>
	<div class="blockSeparated">
		<@s.submit key="label.add"/>
	</div>
</@s.form>