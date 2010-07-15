${action.setPageType('predefined_locations', 'location_list')!}
<#assign currentAction="predefinedLocations.action" />
<#include "/templates/html/common/_formErrors.ftl"/>

<head>
	<@n4.includeStyle type="page" href="locationCrud" />
	<#include "/templates/html/common/_columnView.ftl"/>
	
	<script type="text/javascript">
	   onDocumentLoad(function() {
		   		jQuery('#editLocation').click(function(){
		       		var nodeId = jQuery('#nodeList').getSelectedNode().id;
		       		if(nodeId!=-1){
		       			redirect("<@s.url action="predefinedLocationEdit" />" + "?uniqueID="+nodeId);
		       		}else{
		       			alert("<@s.text name="label.cannot_edit_root"/>")
		       		}
	      	});
	      
	      });
	</script>
	
</head>
<#if helper.hasPredefinedLocationTree()>
	<@n4.hierarchicalList id="nodeList" name="heirarchicalList" nodesList=helper.predefinedLocationTree value="${parentId!}"/>
</#if>
<div class="blockSeparated">
	<@s.submit id="editLocation" name="edit" key="label.edit_location"/>
</div>