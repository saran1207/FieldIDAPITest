
${action.setPageType('asset', 'edit')!}
<@s.form action="assetUpdate" cssClass="fullForm fluidSets" theme="fieldid" id="assetUpdate" >
	<#include "_assetForm.ftl"/>
	<@s.hidden name="customerOrderId" />
	<@s.hidden name="tagOptionId" />
	
	<div class="actions">
		<@s.submit id="saveButton" key="hbutton.save" onclick=" return false;" cssClass="saveButton save"/>
		
		<@s.submit id="saveAndStartEventButton" name="saveAndInspect" key="hbutton.saveandstartevent"  cssClass="saveButton save"/>
		
		<@s.text name="label.or"/>
		<a href="<@s.url action="asset"  uniqueID="${asset.id}"/>"><@s.text name="label.cancel"/></a>
		
		 | 
		<a href="<@s.url action="assetMergeAdd" uniqueID="${asset.id}"/>"><@s.text name="label.merge"/></a>
		
		 | 
		<a href="<@s.url  action="assetConfirmDelete" uniqueID="${asset.id}"/>"><@s.text name="label.delete"/></a>
	</div>
	
</@s.form >
<script type="text/javascript" >
	var buttons = new Array( 'saveButton', 'saveAndStartEventButton' );
	var buttonLockMessages = new Array( '<@s.text name="hbutton.pleasewait"/>', '<@s.text name="hbutton.pleasewait"/>' );
	var buttonMessages = new Array( '<@s.text name="hbutton.save"/>', '<@s.text name="hbutton.saveandstartevent"/>' );
	$$('#assetUpdate .save').each(function(element) {
			element.observe('click', function(event) {
				var element = Event.element(event);
				event.stop();
				saveAsset(element);
			});
		});
</script>