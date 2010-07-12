${action.setPageType('predefined_locations', 'location_list')!}
<#assign currentAction="predefinedLocations.action" />
<#include "/templates/html/common/_formErrors.ftl"/>

<head>
	<@n4.includeStyle type="page" href="locationCrud" />
	<#include "/templates/html/common/_columnView.ftl"/>
</head>

	<#if helper.hasPredefinedLocationTree()>
		<@n4.hierarchicalList id="nodeList" name="heirarchicalList" nodesList=helper.predefinedLocationTree value="${parentId!}"/>
		<head>
			<script type="text/javascript">
			   onDocumentLoad(function() {
			   		jQuery
			   		jQuery('#addLocation, #editLocation').click(function(){
				       	jQuery('#parent, #currentNode').val(jQuery('#nodeList').getSelectedNode().id);
			      	});
			      
			      });
			</script>
		</head>	
	</#if>
<div class="form">
	<@s.form action="predefinedLocationsAdd" method="add" id="predefinedLocationsAdd" theme="fieldid">
		<label><@s.text name="label.add_new_location_description"/></label>
		<@s.textfield id="nodeForm" name="name" />
		<@s.hidden id="parent" name="parentId"/>
		<@s.submit id="addLocation" key="hbutton.add_new_location" cssClass="saveButton save"/>
	</@s.form> 
</div>

<div class="form">	
	<@s.form action="predefinedLocationsUpdate" method="update" id="predefinedLocationsUpdate" theme="fieldid">
		<label><@s.text name="label.edit_location_description"/></label>
		<@s.textfield id="nodeFormEdit" name="name" />
		<@s.hidden id="currentNode" name="nodeId"/>
		<@s.submit id="editLocation" key="hbutton.edit_location" cssClass="saveButton save"/>
	</@s.form> 
</div>
<!--@s.submit class="button" key="hbutton.save" /-->
<!--button class="button" onclick="return redirect( '<@s.url action="setup" currentPage="${currentPage}" />' );" ><@s.text name="label.cancel"/></button!-->
