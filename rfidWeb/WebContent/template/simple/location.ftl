<@s.hidden name="${parameters.name}.predefinedLocationId" id="${parameters.id}_predefinedLocationId"/>

<#if !nodesList?exists || nodesList.empty>
	<@s.textfield id="${parameters.id}_freeformLocation" name="${parameters.name}.freeformLocation" theme="fieldidSimple"/>
<#else>
	<@s.hidden id="${parameters.id}_freeformLocation" name="${parameters.name}.freeformLocation" />
	
	<@s.textfield id="${parameters.id}_locationName" name="locate" theme="fieldidSimple" value="${parameters.fullName!}" readonly="true"/>
	<a href="#" id="${parameters.id}_showLocationSelection"><@s.text name="label.choose"/></a>
	
	
	<div id="${parameters.id}_locationSelection" class="locationSelection offScreen">
		<label for="predefinedLocation"><@s.text name="label.predefined_location"/></label><br/>
		<@n4.heirarchicalList id="${parameters.id}_predefinedLocationSelector" nodesList=helper.predefinedLocationTree name="asset.location.predefinedLocationId" />

		<label for="freeformLocationInput"><@s.text name="label.freeform_location"/></label><br/>
		<@s.textfield id="${parameters.id}_freeformInput" name="freeformLocationInput" value="%{${parameters.name}.freeformLocation}" theme="simple"/>
		<div class="actions">
			<input type="button" name="select" value="<@s.text name="label.select_location"/>" id="${parameters.id}_locationSelection_select"/>
			<@s.text name="label.or"/>
			<a href="#" id="${parameters.id}_locationSelection_cancel"><@s.text name="label.cancel"/></a>
		</div>
	</div>
	
	<@n4.includeScript>
		onDocumentLoad(function() {
				$('${parameters.id}_locationSelection_select').observe('click', function(event) {
					event.stop();
					$('${parameters.id}_locationSelection').setStyle({left:'-10000px'});
					
					var node = jQuery('#${parameters.id}_predefinedLocationSelector').getSelectedNode();
					$('${parameters.id}_predefinedLocationId').value = node.id;
					$('${parameters.id}_freeformLocation').value = $('${parameters.id}_freeformInput').getValue();
					$('${parameters.id}_locationName').value = node.parentNames.join(" ") + " " + node.name + " " + $('${parameters.id}_freeformInput').getValue();
					
					
				});
				
				$('${parameters.id}_locationSelection_cancel').observe('click', function(event) { event.stop(); 
					$('${parameters.id}_locationSelection').setStyle({left:'-10000px'});
					$('${parameters.id}_freeformInput').value = $('${parameters.id}_freeFromLocation').getValue();
					var predefinedLocationId = $('${parameters.id}_predefinedLocationId').getValue(); 
					jQuery("#${parameters.id}_predefinedLocationSelector").selectNode(predefinedLocationId);
					
				});
				
				$('${parameters.id}_showLocationSelection').observe('click', function(event) {
						event.stop();
						$('${parameters.id}_locationSelection').removeClassName('offScreen');
						translate($('${parameters.id}_locationSelection'), $('${parameters.id}_showLocationSelection'), 0, 0);
					});
			});
	</@n4.includeScript>
</#if>