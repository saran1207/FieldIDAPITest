<script type="text/javascript" src="<@s.url value="javascript/safetyNetworkSmartSearch.js" encode='false' includeParams='none'/>"></script>

<span class="safetyNetworkSmartSearch" id="${parameters.id?html}_container">
	<@s.hidden id="linkedProductId" name="${parameters.name?default()?html}"/>
	
	<div id="registerOverNetworkLinkContainer" class="infoSet">
		<a href="#" id="showSmartSearchLink"><@s.text name="label.registeroversafetynetwork" /></a>
	</div>
	
	<div id="networkSmartSearchContainer" class="infoSet" style="display: none;">
		<@s.form action="orgList" id="orgBrowserForm" name="orgBrowserForm" namespace="/ajax" theme="fieldid" cssClass="fullForm" >
			<label class="label" for="smartSearchVendors"><@s.text name="label.vendors"/></label>
			<@s.select id="snSmartSearchVendors" list="parameters.vendorList" listKey="id" listValue="name"/>
			<@s.textfield id="snSmartSearchText" />
			<@s.submit id="snSmartSearchSubmit" key="label.load"/>
			<@s.submit id="snSmartSearchCancel" key="label.cancel" />
		</@s.form>
	</div>
	
	<div id="linkedProductContainer" class="infoSet" style="display: none;">
		
	</div>

</span>

<script type="text/javascript">
	networkSmartSearchUrl = "<@s.url action="safetyNetworkSmartSearch" namespace="/ajax/"/>";
</script>