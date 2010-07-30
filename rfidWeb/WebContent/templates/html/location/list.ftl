${action.setPageType('predefined_locations', 'location_list')!}

<head>
	<#include "/templates/html/common/_columnView.ftl"/>
	<script type="text/javascript">
	   onDocumentLoad(function() {
	  	 	var nodeId = jQuery('#nodeList').getSelectedNode().id;
	   			jQuery('#editLocation').click(function(){
		       		if(nodeId!=-1){
		       			redirect("<@s.url action="predefinedLocationEdit" />" + "?uniqueID="+nodeId);
		       		}else{
		       			alert("<@s.text name="label.cannot_edit_root"/>")
		       		}
	      	});
	 		  jQuery('#editLevel').click(function(){
	 		  	var nodeId = jQuery('#nodeList').getSelectedNode().id;
		       	redirect("<@s.url action="predefinedLocationLevels" />" + "?uniqueID="+nodeId);
	      	});
	   
		   		jQuery('#addLocation').click(function(){
		       	jQuery('#parent').val(jQuery('#nodeList').getSelectedNode().id);
	      	});
	      
	      });
	</script>
</head>	

<@s.form action="predefinedLocationCreate"  theme="fieldid" cssClass="fullForm ">
	<#include "/templates/html/common/_formErrors.ftl"/>
	
	<div class="infoSet">
		<label class='label'><@s.text name="label.title"/></label>
		<@s.textfield id="nodeForm" name="name" />
	</div>

	<@s.hidden id="parent" name="parentId"/>
	<div class="actions">
		<@s.submit id="addLocation" key="hbutton.add_new_location" cssClass="saveButton save"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="predefinedLocations"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form> 

<div class="blockSeparated">
	<@s.submit id="editLocation" name="edit" key="label.edit_location"/>
	<@s.submit id="editLevel" name="editLevel" key="label.edit_level" uniqueID="someValue"/>
</div>

<#if helper.hasPredefinedLocationTree()>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.parent_location"/></label>
		<div class="fieldHolder">
			<@n4.hierarchicalList id="nodeList" name="heirarchicalList" nodesList=helper.predefinedLocationTree value="${parentId!}"/>
		</div>
	</div>
	
</#if>