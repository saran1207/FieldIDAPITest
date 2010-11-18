<#if lineItem?exists>
	${action.setPageType('asset', 'add_with_order')!}
<#else>
	${action.setPageType('asset', 'add')!}
</#if>


<title>
	<@s.text name="${navOptions.title}.plural" />
	<#if lineItem?exists > 
		- <@s.text name="label.ordernumber"/> ${lineItem.order.orderNumber?html}
		( <@s.text name="label.orderqty"/>:  ${lineItem.quantity},
		 <@s.text name="label.identifiedassets"/>: ${action.getIdentifiedAssetCount(lineItem)} )
	</#if>
</title>

<#if limits.assetsMaxed>
	<div class="limitWarning">
	<@s.text name="label.exceeded_your_asset_limit">
		<@s.param>${limits.assetsMax}</@s.param>
	</@s.text>
	</div>
<#else>
	<@s.form action="assetCreate" cssClass="fullForm fluidSets" theme="fieldid" id="assetCreate" >
		<@s.hidden name="lineItemId" />
		<@s.hidden name="tagOptionId" />
		<#include "_assetForm.ftl" />
		
		<div class="actions">
			<@s.submit id="saveButton" name="save" cssClass="save" key="hbutton.save" />
			<#if Session.sessionUser.hasAccess("createevent") >
				| <@s.submit id="saveAndStartEventButton" cssClass="save"  name="saveAndStartEvent" key="hbutton.saveandstartevent" />
			</#if>
			<span id="saveAndPrintAction" <#if !assetType.hasManufactureCertificate > style="display:none"</#if>>
				| <@s.submit id="saveAndPrintButton" cssClass="save"  name="saveAndPrint" key="hbutton.saveandprint" />
			</span>
			<#if Session.sessionUser.hasAccess("createevent")>
				| <@s.submit id="saveAndScheduleButton" cssClass="save"  name="saveAndSchedule" key="hbutton.saveandschedule"  />
			</#if>
		</div>
	</@s.form>
</#if>

<script type="text/javascript" >
	var buttons = new Array( 'saveButton', 'saveAndInspButton', 'saveAndPrintButton','saveAndScheduleButton');
	var buttonLockMessages = new Array( '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />' );
	var buttonMessages = new Array( '<@s.text name="hbutton.save" />', '<@s.text name="hbutton.saveandstartevent" />', '<@s.text name="hbutton.saveandprint" />', '<@s.text name="hbutton.saveandschedule" />' );
	
	$$('#assetCreate .save').each(function(element) {
			element.observe('click', function(event) {
				var element = Event.element(event);
				event.stop();
				checkDuplicateRfids('rfidNumber', element);
			});
		});
	
</script>
