<script type="text/javascript" src="<@s.url value="javascript/safetyNetworkSmartSearch.js" encode='false' includeParams='none'/>"></script>
<link rel="stylesheet" type="text/css" href="<@s.url value="/style/featureStyles/safetyNetworkSmartSearch.css"/>" />

<span class="safetyNetworkSmartSearch" id="${parameters.id?html}_container">
	<@s.hidden id="linkedProductId" name="${parameters.name?default()?html}"/>
	
	<div id="registerOverNetworkLinkContainer" class="infoSet">
		<a href="#" id="showSmartSearchLink"><@s.text name="label.registeroversafetynetwork" /></a>
	</div>
	
	<div id="networkSmartSearchContainer" class="infoSet" style="display: none;">
		<@s.form action="orgList" id="orgBrowserForm" name="orgBrowserForm" namespace="/ajax" theme="fieldid" cssClass="fullForm" >
			<label class="label" for="smartSearchVendors"><@s.text name="label.vendors"/>: </label>
			<@s.select id="snSmartSearchVendors" list="parameters.vendorList" listKey="id" listValue="name"/>
			<@s.textfield id="snSmartSearchText" />
			<@s.submit id="snSmartSearchSubmit" key="label.load"/>
			<@s.submit id="snSmartSearchCancel" key="label.cancel" />
			<span id="smartSearchStatus"></span>
		</@s.form>
	</div>
	
	<div id="linkedProductContainer" class="infoSet" style="display: none;">
		<h4 class="productInfoTitle"><@s.text name="label.linkedproductinfo"/></h4>
		<div class="leftColumn" style="float: left;">
			<div class="infoSet" >
				<label class="label" for="linkedProductSerial"><@s.text name="label.serialnumber"/>: </label>
				<span id="linkedProductSerial"></span>
			</div>
			<div class="infoSet" >
				<label class="label" for="linkedProductRfid"><@s.text name="label.rfidnumber"/>: </label>
				<span id="linkedProductRfid"></span>
			</div>
		</div>
		<div class="rightColumn">
			<div class="infoSet" >
				<label class="label" for="linkedProductOwner"><@s.text name="label.vendor"/>: </label>
				<span id="linkedProductOwner"></span>
			</div>
			<div class="infoSet" >
				<label class="label" for="linkedProductType"><@s.text name="label.producttype"/>: </label>
				<span id="linkedProductType"></span>
			</div>
		</div>
		<a href="#" id="unregisterSubmit"><@s.text name="label.unregister" /></a>
	</div>

</span>

<script type="text/javascript">
	networkSmartSearchUrl = "<@s.url action="safetyNetworkSmartSearch" namespace="/ajax/"/>";
	
	<#-- if were on edit and there's already a linked product, we need to go directly to show linked product info -->
	<#if parameters.linkedProduct_editMode >
		var product = new Object();
	
		product.id = ${parameters.linkedProduct_Id};
		product.serialNumber = "${(parameters.linkedProduct_SerialNumber)?default("")}";
		product.rfidNumber = "${(parameters.linkedProduct_RfidNumber)?default("")}";
		product.owner = "${(parameters.linkedProduct_OwnerName)?default("")}";
		product.type = "${(parameters.linkedProduct_TypeName)?default("")}";
	
		updateLinkedProductInfo(product);
	</#if>
</script>