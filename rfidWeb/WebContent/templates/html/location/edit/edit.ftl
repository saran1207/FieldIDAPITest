${action.setPageType('predefined_locations', 'location_edit')!}
<#include "/templates/html/common/_formErrors.ftl"/>

<head>
	<@n4.includeStyle type="page" href="locationCrud" />
	<#include "/templates/html/common/_columnView.ftl"/>
	
	<script type="text/javascript">
		onDocumentLoad(function() {
			jQuery('#nodeList').click(function(event){
				 if (jQuery(event.target).is('a') || jQuery(event.target).parent('a')){
				 	 jQuery('#nodeFormEdit').attr("value",jQuery('#nodeList').getSelectedNode().name);
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
<@s.form action="predefinedLocationDoEdit" method="edit" id="predefinedLocationDoEdit" theme="fieldid" cssClass="crudForm">
	<p>
		<label><@s.text name="label.new_title"/></label>
		<span>
			<@s.textfield id="nodeFormEdit" name="name" />
		</span>
	</p>
	<@s.hidden id="currentNode" name="nodeId"/>
	<div class="formAction">
		<button class="button" onclick="return redirect( '<@s.url action="predefinedLocations" currentPage="${currentPage}" />' );" ><@s.text name="label.cancel"/></button>
		<@s.submit id="editLocation" type="submit" key="hbutton.edit_location" cssClass="saveButton save"/>
	</div>
</@s.form> 
