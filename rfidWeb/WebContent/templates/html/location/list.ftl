${action.setPageType('predefined_location', 'location_list')!}
<#assign currentAction="predefinedLocations.action" />
<#include "/templates/html/common/_formErrors.ftl"/>

<head>
	<@n4.includeStyle type="page" href="locationCrud" />
	<#include "/templates/html/common/_columnView.ftl"/>
	<script type="text/javascript" src="<@s.url value="/javascript/location.js"/>"></script>
</head>

<@s.form action="predefinedLocationsUpdate" id="locationUpdate">
	
	<@n4.hierarchicalList id="nodeList" name="heirarchicalList" nodesList=helper.predefinedLocationTree/>
	<@s.textfield id="nodeForm" name="name" />
	<@s.hidden id="parent" name="parentId"/>
	<!--input type="button" name="getactive" id="getactive" value="<@s.text name="label.add_new_location"/>"/-->
	<@s.submit id="addLocation" key="hbutton.save" cssClass="saveButton save"/>
</@s.form> 

<script type="text/javascript">
	   jQuery('#addLocation').click(function(){
       	jQuery('#parent').val(jQuery('#nodeList').getSelectedNode().id);
       	jQuery('#nodeList').selectNode('#parent');
      });
</script>