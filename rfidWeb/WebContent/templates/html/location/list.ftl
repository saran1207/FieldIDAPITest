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
				   		jQuery('#addLocation').click(function(){
				       	jQuery('#parent').val(jQuery('#nodeList').getSelectedNode().id);
			      	});
			      
			      });
			</script>
		</head>	
	</#if>
	
<label><@s.text name="label.add_new_location_description"/></label>
<@s.form action="predefinedLocationsAdd" method="add" id="predefinedLocationsAdd" theme="fieldid">
	<@s.textfield id="nodeForm" name="name" />
	<@s.hidden id="parent" name="parentId"/>
	<@s.submit id="addLocation" key="hbutton.add_new_location" cssClass="saveButton save"/>
</@s.form> 
