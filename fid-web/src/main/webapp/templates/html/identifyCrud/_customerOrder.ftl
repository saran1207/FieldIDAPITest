<div id="assets" >
	<h2 class="orderTitle" ><@s.text name="label.attachassettoorder"/></h2>
	<div >
		<div id="assetLookup">
			<#assign namespace="/ajax"/>
			<#assign assetSearchAction="customerOrderFindAsset"/>
			<#assign assetFormId="assetSearch"/>
			<#include "../eventGroup/_searchForm.ftl"/>
			<div id="assetResults"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
    onDocumentLoad(function() {
        $$( "form[name='customerOrderFindAsset']" )[0].observe( 'submit',
                function(event) {
                    event.stop();
                    var element = Event.element( event );
                    element.request( getStandardCallbacks() );
                } );
    });

	function marryCustomerOrder(baseUrl) {
		getResponse(baseUrl + "&customerOrderId=${order.id}");
	}
</script>