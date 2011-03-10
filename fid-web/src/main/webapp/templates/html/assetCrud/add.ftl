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
		<#assign isAddForm=true>
		<#include "_assetForm.ftl" />
		
		<div class="actions">
			<@s.submit id="saveButton" name="save" cssClass="save" key="hbutton.save" />
			<#if Session.sessionUser.hasAccess("createevent") >
				<@s.submit id="saveAndStartEventButton" cssClass="save"  name="saveAndStartEvent" key="hbutton.saveandstartevent" />
			</#if>
			<span id="saveAndPrintAction">
				<@s.submit id="saveAndPrintButton" cssClass="save"  name="saveAndPrint" key="hbutton.saveandprint" />
			</span>
			<@s.text name="label.or"/>
			<a href="<@s.url action="home"/>"><@s.text name="label.cancel"/></a>
			
		</div>
	</@s.form>
</#if>

<script type="text/javascript" >
	var buttons = new Array( 'saveButton', 'saveAndStartEventButton', 'saveAndPrintButton');
	var buttonLockMessages = new Array( '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />');
	var buttonMessages = new Array( '<@s.text name="hbutton.save" />', '<@s.text name="hbutton.saveandstartevent" />', '<@s.text name="hbutton.saveandprint" />');
	
	$$('#assetCreate .save').each(function(element) {
			element.observe('click', function(event) {
				var element = Event.element(event);
				event.stop();
				checkDuplicateRfids('rfidNumber', element);
			});
		});
</script>
