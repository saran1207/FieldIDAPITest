${action.setPageType('predefined_locations', 'location_edit')!}
<@s.form action="predefinedLocationUpdate" theme="fieldid" cssClass="fullForm">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden id="currentNode" name="uniqueID"/>
	<div class="infoSet">
		<label class='label'><@s.text name="label.location_name"/></label>
		<@s.textfield id="name" name="name" />
	</div>
	<div class="formActions">
		<@s.submit id="editLocation" type="submit" key="hbutton.save" cssClass="saveButton save"/>
		
		<@s.text name="label.or"/>
		<@s.url id="deleteUrl" action="predefinedLocationDelete" uniqueID="${uniqueID}" />
		<a onclick="return confirm('${action.getText('warning.removeselectedlocation')}');"  href="${deleteUrl}" ><@s.text name="label.remove"/></a>
		<@s.text name="label.or"/>
		<a href="<@s.url action="predefinedLocations" currentPage="${currentPage}" />" ><@s.text name="label.cancel"/></a>
	</div>
</@s.form> 


