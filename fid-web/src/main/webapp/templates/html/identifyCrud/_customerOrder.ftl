<div id="products" >
	<h2 class="orderTitle" ><@s.text name="label.attachproducttoorder"/></h2>
	<div >
		<div id="assetLookup">
			<#assign namespace="/ajax"/>
			<#assign assetSearchAction="customerOrderFindAsset"/>
			<#assign assetFormId="assetSearch"/>
			<#include "../inspectionGroup/_searchForm.ftl"/>
			<div id="productResults"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
$( 'assetSearch' ).observe( 'submit', 
	function( event ) {
		event.stop(); 
		var element = Event.element( event ); 
		element.request( getStandardCallbacks() );
	} );
	
	function marryCustomerOrder(baseUrl) {
		getResponse(baseUrl + "&customerOrderId=${order.id}");
	}
</script>