
<div class="safetyNetworkSmartSearch" id="${parameters.id?html}_container">
	<#if !parameters.vendorList.empty>
		<@s.hidden id="linkedProductId" name="${parameters.name?default()?html}"/>
		
		<div id="registerOverNetworkLinkContainer" >
			<a href="#" id="showSmartSearchLink"><@s.text name="label.registeroversafetynetwork" /></a>
		</div>
		
		<div id="linkedProductContainer"  style="display: none;">
			<h4 class="productInfoTitle">
				<@s.text name="label.registered_against"/> <span id="linkedProductType"></span> <@s.text name="label.from"/> <span id="linkedProductOwner"></span>
			</h4>
			<div  >
				<label for="linkedProductSerial"><@s.text name="label.serialnumber"/>: </label>
				<span id="linkedProductSerial"></span>
			</div>
			<div  >
				<label  for="linkedProductRfid"><@s.text name="label.rfidnumber"/>: </label>
				<span id="linkedProductRfid"></span>
			</div>
		
			<a href="#" id="unregisterSubmit"><@s.text name="label.unregister" /></a>
		</div>
	<#else>
		<div id="registerOverNetworkLinkContainer" >
			<@s.text name="label.no_vendors_in_your_safety_network" />
		</div>
		
	</#if>
</div>

<script type="text/javascript">
	<#assign snSmartSearch>
    	<div id="networkSmartSearchContainer"style="display: none;">
			<@s.form action="safetyNetworkSmartSearch" id="snSmartSearch" name="snSmartSearch" namespace="/ajax" theme="fieldid" cssClass="fullForm" >
				<label class="label" for="smartSearchVendors"><@s.text name="label.vendor"/></label>
				<@s.select id="snSmartSearchVendors" name="vendorId" list="parameters.vendorList" listKey="id" listValue="name"/>
				<label class="label" for="snSmartSearchText"><@s.text name="label.smart_search_search_types"/></label>
				<@s.textfield id="snSmartSearchText" name="searchText"/>
				<div class="actions">
					<@s.submit id="snSmartSearchSubmit" key="label.load"/>
					<@s.text name="label.or"/>
					<a href="#" id="snSmartSearchCancel"><@s.text name="label.cancel" /></a>
				</div>
			</@s.form>
			<div id="snSmartSearchResults"></div>
		</div>
	</#assign>
	snSmartSearch = '${snSmartSearch?js_string}';
	<#-- if were on edit and there's already a linked product, we need to go directly to show linked product info -->
	<#if parameters.linkedProduct_editMode >
		document.observe("dom:loaded", function() {
				var product = new Object();
			
				product.id = ${parameters.linkedProduct_Id};
				product.serialNumber = "${(parameters.linkedProduct_SerialNumber)?default("")}";
				product.rfidNumber = "${(parameters.linkedProduct_RfidNumber)?default("")}";
				product.owner = "${(parameters.linkedProduct_OwnerName)?default("")}";
				product.type = "${(parameters.linkedProduct_TypeName)?default("")}";
			
				updateLinkedProductInfo(product);
			});
		</#if>
</script>