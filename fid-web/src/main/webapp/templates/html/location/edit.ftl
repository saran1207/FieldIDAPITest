${action.setPageType('predefined_locations', 'location_edit')!}
<head>
	<@n4.includeStyle type="page" href="location" />
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>
<div class="addBox">
	<@s.form action="predefinedLocationUpdate" theme="fieldid" cssClass="fullForm">
		<#include "/templates/html/common/_formErrors.ftl"/>
		<@s.hidden id="currentNode" name="uniqueID"/>
		<div class="editContent">
			<div class="addLocationFields infoSet">
				<label class='label'><@s.text name="label.location_name"/></label>
				<@s.textfield id="name" name="name" />
                <#if !predefinedLocation.parent?exists>
                    <@n4.orgPicker name="owner" theme="fieldid" id="ownerId"/>
                </#if>
				<div class="formActions buttonGroup">
					<@s.submit id="editLocation" type="submit" key="hbutton.save" cssClass="saveButton save"/>
					<@s.text name="label.or"/>
					<@s.url id="deleteUrl" action="predefinedLocationDelete" uniqueID="${uniqueID}" />
					<a onclick="return confirm('${action.getText('warning.removeselectedlocation')}');"  href="${deleteUrl}" ><@s.text name="label.remove"/></a>
					<@s.text name="label.or"/>
					<a href="<@s.url action="predefinedLocations" currentPage="${currentPage}" />" ><@s.text name="label.cancel"/></a>
				</div>
			</div>
		
		</div>
	</@s.form> 
</div>
