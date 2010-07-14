${action.setPageType('predefined_locations', 'location_edit')!}

<@s.form action="predefinedLocationUpdate" theme="fieldid" cssClass="fullForm">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden id="currentNode" name="uniqueID"/>
	<div class="infoSet">
		<label class='label'><@s.text name="label.title"/></label>
		<@s.textfield id="name" name="name" />
	</div>
	<div class="actions">
		<@s.submit id="editLocation" type="submit" key="hbutton.save" cssClass="saveButton save"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="predefinedLocations" currentPage="${currentPage}" />" ><@s.text name="label.cancel"/></a>
	</div>
</@s.form> 
