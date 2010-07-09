${action.setPageType('predefined_location_levels', 'list')!}

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
				<td><a href="#" class="edit" ><@s.text name="edit"/></a></td>
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


<@s.form action="predefinedLocationLevelCreate" theme="fieldid" cssClass="largeForm">
<@s.hidden name="levelName.index" value="${levels.size()}"/>
<div class="infoSet">
	<label class="label"><@s.text name="label.name"/></label>
	<@s.textfield name="levelName.name"/>
</div>
<div class="action">
	<@s.submit key="label.add"/>
</div>
</@s.form>

<head>
	<@n4.includeScript>
		onDocumentLoad(function() {
			$$('#removeLast').each(function(e){ 
				e.observe('click', function(event) {
					event.stop();
					postForm('<@s.url action="predefinedLocationLevelDelete"/>', {'levelName.index' : ${levels.size() -1}});
				});
			});
			
			
			$$('#levels .cancel').each(function(e){
				e.observe('click', function(event) {
					event.stop();
					var element = Event.element(event);
					var nameCell = element.up('td');
					
					nameCell.down('.name').show();
					nameCell.down('.levelForm').hide().reset();
				}); 
			});
			$$('#levels .edit').each(function(e){
				e.observe('click', function(event) {
					event.stop();
					var element = Event.element(event);
					var row = element.up('tr');
					
					row.down('.name').hide();
					row.down('.levelForm').show();
					row.down('.levelForm').findFirstElement().focus().select();
				}); 
			});
		});
	</@n4.includeScript>
</head>