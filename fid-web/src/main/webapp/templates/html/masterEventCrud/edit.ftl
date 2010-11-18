<head>
	<script type="text/javascript" src="<@s.url value="/javascript/subAsset.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/event.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/masterEvent.js"/>"></script>
	<@n4.includeStyle type="page" href="subAsset" />
	<@n4.includeStyle type="page" href="event" />
	<@n4.includeStyle type="page" href="masterEvent" />
</head>


<#assign form_action="EDIT" /> 
${action.setPageType('event', 'edit')!}

<div id="masterEvent" >
	<#include "/templates/html/common/_formErrors.ftl" />
	
	<div class="masterAsset done">
		<div class="definition"><div class="identifier"><span>${asset.type.name!}</span></div></div>
		<div class="performedEvent">
			<span>${(eventType.name)!}</span> 
			<span>
				<a class="exitLink" href="<@s.url action="subEventEdit"  uniqueID="0" assetId="${asset.id}" type="${type}" parentAssetId="${asset.id}" token="${token}"/>">
					<@s.text name="label.edit_this_event"/>
				</a>
			</span>
		</div>
	</div>
	
	
	<div id="assetComponents">
		<#list availableSubAssets as subAsset>
			<#include "_subEvent.ftl" />
		</#list>
	</div>
	
	<@s.form action="masterEventUpdate" id="subAssetForm" cssClass="crudForm" theme="fieldid">
		<@s.hidden name="uniqueID" id="uniqueID"/>
		<@s.hidden name="token"/>
		<@s.hidden name="type"/>
		<@s.hidden name="eventGroupId"/>
		<@s.hidden name="assetId" id="assetId"/>
		<div class="formAction">
			<@s.submit key="label.save" />
			<@s.text name="label.or"/>
			<@s.url id="cancelUrl" action="eventGroups" uniqueID="${asset.id}"/>
			<@s.url id="deleteUrl" action="masterEventDelete" uniqueID="${uniqueID}" assetId="${assetId}" /> 
			<a href="#" onclick="return redirect( '${cancelUrl}' );"/><@s.text name="label.cancel"/></a>
			<@s.text name="label.or"/>
			<a href="#" onclick="javascript: if (window.confirm( '<@s.text name="label.confirm_event_delete"/>' )) return redirect( '${deleteUrl}' );" ><@s.text name='label.delete'/></a>
		</div>
	</@s.form>
</div>


