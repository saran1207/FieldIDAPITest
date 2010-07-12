${action.setPageType('predefined_locations', 'location_edit')!}
<#include "/templates/html/common/_formErrors.ftl"/>

<head>
	<@n4.includeStyle type="page" href="locationCrud" />
	<#include "/templates/html/common/_columnView.ftl"/>
</head>

<#if helper.hasPredefinedLocationTree()>
	<@n4.hierarchicalList id="nodeList" name="heirarchicalList" nodesList=helper.predefinedLocationTree value="${nodeId!}"/>
	<head>
		<script type="text/javascript">
		   onDocumentLoad(function() {
		   		jQuery('#editLocation').click(function(){
			       	jQuery('#currentNode').val(jQuery('#nodeList').getSelectedNode().id);
		      	});
		      });
		</script>
	</head>	
</#if>
<label><@s.text name="label.edit_location_description"/></label>
<@s.form action="predefinedLocationDoEdit" method="edit" id="predefinedLocationDoEdit" theme="fieldid">
	<@s.textfield id="nodeFormEdit" name="name" />
	<@s.hidden id="currentNode" name="nodeId"/>
	<@s.submit id="editLocation" key="hbutton.edit_location" cssClass="saveButton save"/>
	<button class="button" onclick="return redirect( '<@s.url action="predefinedLocations" currentPage="${currentPage}" />' );" ><@s.text name="label.cancel"/></button>
</@s.form> 
