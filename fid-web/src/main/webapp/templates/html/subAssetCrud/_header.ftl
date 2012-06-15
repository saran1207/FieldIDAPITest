<div class="definition" id="subAssetDefinition_${subAsset.asset.id}">
	<div class="identifier">
		<div class="drag" style="display:none;" ><img src="<@s.url value="/images/drag.gif"/>" alt="<@s.text name="label.drag"/>"/></div>
		<div class="assetType">${(subAsset.asset.type.name?html)!}</div>
		<div class="subAssetLabel ">${(subAsset.label?html)!} <a href="#change" class="notAllowedDuringOrdering" onclick="$('subAssetDefinition_${subAsset.asset.id}').hide(); $('subAssetLabelForm_${subAsset.asset.id}').show(); return false;"><@s.text name="label.change_label"/></a></div>
	</div>
	<div class="subAssetActions">
		<#if inEvent?exists>
			<div id="subEvent_${subAsset.asset.id}" class="subAssetAction eventTypes simpleContainer">
				<a class="exitLink" href="javascript:void(0);"  ><@s.text name="label.start_event"/></a>
				<ul id="subEvent_${subAsset.asset.id}_list" >
					<#assign eventTypes=subAsset.asset.type.eventTypes  />
					<#assign subEvent=true/>
					<#include "/templates/html/eventGroup/_eventSelect.ftl" />
				</ul>
			</div>
		</#if>
	
		<div class="subAssetAction removeSubAsset">
			<a href="#removeSubAsset" id="removeSubAsset_${subAsset.asset.id}" onclick="removeSubAsset( '${subAsset.asset.id}' ); return false;"><img src="<@s.url value="/images/x.gif"/>" alt="<@s.text name="label.remove"/>"/></a>
		</div>
	</div>
</div>


<div class="labelForm" style="display:none" id="subAssetLabelForm_${subAsset.asset.id}">
	<@s.form action="${updateSubAssetAction!'updateSubAsset'}" id="subAssetForm_${subAsset.asset.id}" namespace="/ajax" theme="fieldid">
		<@s.hidden name="subAsset.asset.type.name" value="${subAsset.asset.type.name!}" />
		<@s.hidden name="subAsset.asset.iD" value="${subAsset.asset.id}"/>
		<@s.hidden name="subAsset.asset.identifier" value="${subAsset.asset.identifier!}"/>
		<@s.hidden name="uniqueID" value="${uniqueID}"/>
		<@s.hidden name="token"/>
		<label for="label"><@s.text name="label.label"/></label>  
		<@s.textfield name="subAsset.label" value="${subAsset.label!}"/>
		<@s.submit key="label.save"/>
		<a href="#" onclick="$('subAssetDefinition_${subAsset.asset.id}').show(); $('subAssetLabelForm_${subAsset.asset.id}').hide(); $('subAssetForm_${subAsset.asset.id}').reset(); return false;"><@s.text name="label.cancel"/></a>
	</@s.form>
	
</div>


<script type="text/javascript">
	$('subAssetForm_${subAsset.asset.id}').observe('submit', function(event) { event.stop(); $('subAssetForm_${subAsset.asset.id}').request(getStandardCallbacks()); } );
</script>	

