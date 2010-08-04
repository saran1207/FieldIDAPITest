${action.setPageType('predefined_locations', 'location_list')!}

<head>
	<#include "/templates/html/common/_columnView.ftl"/>
	<@n4.includeStyle type="page" href="location" />
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
	 	    jQuery('#editLevel').click(function(){
	 		  		var nodeId = jQuery('#nodeList').getSelectedNode().id;
		       		redirect("<@s.url action="predefinedLocationLevels" />" + "?uniqueID="+nodeId);
	      	});
	   
		   	jQuery('.addLocation').click(function(){
		   		alert("hurf");
		       	jQuery('#parent').val(jQuery('#nodeList').getSelectedNode().id);
	      	});
	      
	      });
	</script>
</head>	

<#if helper.hasPredefinedLocationTree()>
	<div class="addBox">
	<@s.form action="predefinedLocationCreate"  theme="fieldid" cssClass="fullForm ">
		<@s.hidden id="parent" name="parentId"/>
		<#include "/templates/html/common/_formErrors.ftl"/>
		<div class="addLocationContent">
			<div class="infoSet">
				<label class='label'><@s.text name="label.location_name"/>	</label>
				<@s.textfield id="nodeForm" name="name" />
				<@s.submit id="addLocation" key="hbutton.add_new_location" cssClass="saveButton save"/>
			</div>
		</div>
	</@s.form> 
	</div>

	<div id="editButtons" class="formAction">
		<h2 class="treePreview"><@s.text name="label.preview_of_predefined_locations"/></h2>
		<@s.submit id="editLevel" name="editLevel" key="label.edit_level" uniqueID="someValue"/>
		<@s.submit id="editLocation" name="edit" key="label.edit_location"/>
	</div>

	<div class="treeBox">
		<div class="infoSet">
			<div class="fieldHolder">
				<@n4.hierarchicalList id="nodeList" name="heirarchicalList" nodesList=helper.predefinedLocationTree value="${parentId!}"/>
			</div>
		</div>
	</div>
<#else>
	<div id="initialSetup">
		<div id="initialContent">
			<h1 class="intialHeading"><@s.text name="label.ready_to_setup_locations"/></h1>
			<div class="locationDesc"><@s.text name="label.location_description"/></div>
			<@s.form action="predefinedLocationCreate"  theme="fieldid" cssClass="fullForm">
				<@s.hidden id="parent" name="parentId"/>
				<#include "/templates/html/common/_formErrors.ftl"/>
				<div class="infoSet">
					<@s.textfield id="addFirstForm" name="name" />
					<@s.submit id="addFirstLocation" key="hbutton.create_first_location" cssClass="saveButton save"/>
				</div>
			</@s.form> 
		</div>
	</div>
</#if>