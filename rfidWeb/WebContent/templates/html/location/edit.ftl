${action.setPageType('predefined_locations', 'location_edit')!}
<#include "/templates/html/common/_formErrors.ftl"/>

<head>
	<@n4.includeStyle type="page" href="locationCrud" />
	<#include "/templates/html/common/_columnView.ftl"/>
	
	<script type="text/javascript">
		onDocumentLoad(function() {
			jQuery('#nodeList').click(function(event){
				 if (jQuery(event.target).is('a') || jQuery(event.target).parent('a')){
				 	 jQuery('#name').attr("value",jQuery('#nodeList').getSelectedNode().name);
				 }
			});
			
	   		jQuery('#editLocation').click(function(){
		       	jQuery('#currentNode').val(jQuery('#nodeList').getSelectedNode().id);
	      	});
	   });
	</script>
</head>

<#if helper.hasPredefinedLocationTree()>
	<@n4.hierarchicalList id="nodeList" name="heirarchicalList" nodesList=helper.predefinedLocationTree value="${nodeId!}"/>
</#if>
<@s.form action="predefinedLocationDoEdit" id="predefinedLocationDoEdit" theme="fieldid" cssClass="fullForm fluidSets">
	<@s.hidden id="currentNode" name="nodeId"/>
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
