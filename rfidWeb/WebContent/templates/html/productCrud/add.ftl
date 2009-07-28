<#if lineItem?exists>
	${action.setPageType('product', 'add_with_order')!}
<#else>
	${action.setPageType('product', 'add')!}
</#if>
<title>
	<@s.text name="${navOptions.title}.plural" />
	<#if lineItem?exists > 
		- <@s.text name="label.ordernumber"/> ${lineItem.order.orderNumber?html}
		( <@s.text name="label.orderqty"/>:  ${lineItem.quantity},
		 <@s.text name="label.identifiedproducts"/>: ${action.getIdentifiedProductCount(lineItem)} )
	</#if>
</title>

<#if limits.assetsMaxed>
	<div class="limitWarning">
	<@s.text name="label.exceeded_your_asset_limit">
		<@s.param>${limits.assetsMax}</@s.param>
	</@s.text>
	</div>
<#else>
	<@s.form action="productCreate" cssClass="inputForm" theme="css_xhtml" >
		<#include "_productForm.ftl" />
		<@s.hidden name="lineItemId" />
		<@s.hidden name="tagOptionId" />
		<div class="formAction">
			<@s.reset key="hbutton.reset" onclick="clearForm(this.form); return false;" />
			<@s.submit id="saveButton" name="save" key="hbutton.save" onclick="checkDuplicateRfids('rfidNumber', this); return false;"/>
			<#if Session.sessionUser.hasAccess("createinspection") >
				<@s.submit id="saveAndInspButton" name="saveAndInspect" key="hbutton.saveandinspect" onclick="checkDuplicateRfids('rfidNumber', this); return false;"/>
			</#if>
			<@s.submit id="saveAndPrintButton" name="saveAndPrint" key="hbutton.saveandprint" onclick="checkDuplicateRfids('rfidNumber', this); return false;" />
			<#if Session.sessionUser.hasAccess("createinspection") >
				<@s.submit id="saveAndScheduleButton" name="saveAndSchedule" key="hbutton.saveandschedule" onclick="checkDuplicateRfids('rfidNumber', this); return false;" />
			</#if>
		</div>
	</@s.form>
</#if>

<script type="text/javascript" >
	var buttons = new Array( 'saveButton', 'saveAndInspButton', 'saveAndPrintButton','saveAndScheduleButton');
	var buttonLockMessages = new Array( '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />', '<@s.text name="hbutton.pleasewait" />' );
	var buttonMessages = new Array( '<@s.text name="hbutton.save" />', '<@s.text name="hbutton.saveandinspect" />', '<@s.text name="hbutton.saveandprint" />', '<@s.text name="hbutton.saveandschedule" />' );
</script>