${action.setPageType('predefined_locations', 'level_list')!}
<head>

	<@n4.includeScript src="predefinedLocationLevels"/>
	<@n4.includeScript>
		lastIndex = ${levels.size() - 1};
		deleteLevelUrl = '<@s.url action="predefinedLocationLevelDelete"/>';
	</@n4.includeScript>
</head>

<@s.form action="predefinedLocationLevelCreate" theme="fieldid" cssClass="fullForm fluidSets pageSection">
	<h2 ><@s.text name="label.add_level"/></h2>
	<@s.hidden name="levelName.index" value="${levels.size()}"/>
	<div class="infoSet">
		<label class="label"><@s.text name="label.name"/></label>
		<@s.textfield name="levelName.name"/>
	</div>
	<div class="blockSeparated">
		<@s.submit key="label.add"/>
	</div>
</@s.form>
<div class="blockSeparated"> 
	<#if !levels.isEmpty() >
		<table class="list" id="levels">
			<tr>
				<th><@s.text name="label.name" /></th>
				<th><@s.text name="label.level_number"/></th>
				<th></th>
			<tr>
			
			<#list levels as level>
				<tr id="level_${level_index}">
					<td>
						<span class="name">${level.name?html}</span>
						<@s.form cssClass="hide levelForm" theme="simple" action="predefinedLocationLevelUpdate">
							<@s.textfield name="levelName.name" value="${level.name}"/>
							<@s.hidden name="levelName.index" value="${level_index}"/> 
							<@s.submit id="submit_level_${level_index}" key="label.submit"/>
							<@s.text name="label.or"/>
							<a href="#" class="cancel"><@s.text name="label.cancel"/></a>
						</@s.form>
					</td>
					<td>${level_index + 1}</td>
					<td>
						<a href="#" class="edit" ><@s.text name="edit"/></a>
					</td>
				</tr>
			</#list>
		</table>
		<div >
			<@s.submit key="label.remove_last" id="removeLast" theme="fieldid"/>
		</div>
	<#else >
		<div class="emptyList" >
			<h2><@s.text name="label.noresults"/></h2>
			<p>
				<@s.text name="label.no_levels_defined" />
			</p>
		</div>
	</#if>
</div>
