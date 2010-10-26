
<div class="safetyNetworkSmartSearch" id="${parameters.id?html}_container">
	<#if !parameters.vendorList.empty>
		<@s.hidden id="linkedAssetId" name="${parameters.name?default()?html}"/>
		
		<div id="registerOverNetworkLinkContainer" >
			<a href="#" id="showSmartSearchLink"><@s.text name="label.registeroversafetynetwork" /></a>
		</div>
		
		<div id="linkedAssetContainer"  style="display: none;">
			<h4 class="assetInfoTitle">
				<@s.text name="label.registered_against"/> <span id="linkedAssetType"></span> <@s.text name="label.from"/> <span id="linkedAssetOwner"></span>
			</h4>
			<div  >
				<label for="linkedAssetSerial"><@s.text name="label.serialnumber"/>: </label>
				<span id="linkedAssetSerial"></span>
			</div>
			<div  >
				<label  for="linkedAssetRfid"><@s.text name="label.rfidnumber"/>: </label>
				<span id="linkedAssetRfid"></span>
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
	<#-- if were on edit and there's already a linked asset, we need to go directly to show linked asset info -->
	<#if parameters.linkedAsset_editMode >
		document.observe("dom:loaded", function() {
				var asset = new Object();
			
				asset.id = ${parameters.linkedAsset_Id};
				asset.serialNumber = "${(parameters.linkedAsset_SerialNumber)?default("")?js_string}";
				asset.rfidNumber = "${(parameters.linkedAsset_RfidNumber)?default("")?js_string}";
				asset.owner = "${(parameters.linkedAsset_OwnerName)?default("")?js_string}";
				asset.type = "${(parameters.linkedAsset_TypeName)?default("")?js_string}";
				asset.referenceNumber = "${(parameters.linkedAsset_ReferenceNumber)?default("")?js_string}";
				<#if parameters.refreshRegistration>
					updateLinkedAssetInfo(asset);
				<#else>
					populateLinkedAssetInfo(asset);
				</#if>
				
			});
		</#if>
</script>