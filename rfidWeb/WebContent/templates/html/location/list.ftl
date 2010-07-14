${action.setPageType('predefined_locations', 'location_list')!}
<#assign currentAction="predefinedLocations.action" />
<#include "/templates/html/common/_formErrors.ftl"/>

<head>
	<@n4.includeStyle type="page" href="locationCrud" />
	<#include "/templates/html/common/_columnView.ftl"/>
	
	<script type="text/javascript">
	   onDocumentLoad(function() {
		   		jQuery('#addLocation').click(function(){
		       	jQuery('#parent').val(jQuery('#nodeList').getSelectedNode().id);
	      	});
	      
	      });
	</script>
	
</head>
<#if helper.hasPredefinedLocationTree()>
	<@n4.hierarchicalList id="nodeList" name="heirarchicalList" nodesList=helper.predefinedLocationTree value="${parentId!}"/>
</#if>
	
<@s.form action="predefinedLocationsAdd"  id="predefinedLocationsAdd" theme="fieldid" cssClass="fullForm fluidSets">
	<div class="infoSet">
		<label class='label'><@s.text name="label.title"/></label>
		<@s.textfield id="nodeForm" name="name" />
	</div>

	<@s.hidden id="parent" name="parentId"/>
		<div class="actions">
			<@s.submit id="addLocation" key="hbutton.add_new_location" cssClass="saveButton save"/>
		</div>
</@s.form> 
