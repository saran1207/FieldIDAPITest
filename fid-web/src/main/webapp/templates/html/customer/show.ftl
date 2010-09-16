<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
	<@n4.includeStyle href="safetyNetworkCustomer" type="page"/>
</head>

<#include '_customerInfo.ftl'>

<div id="mainContent">

	<h1>${customer.name}</h1>
	<p>
        <@s.text name="label.customer_connection"/>
    </p>
    <br/><br/>

    <div class="assetsCount registeredAssetsCount">
        ${numRegisteredAssets}
    </div>

    <div class="countExplanation">
        <h2>
            <@s.text name="label.assets_registered"/>
        </h2>

        <p>
            <@s.text name="label.assets_registered_info">
               <@s.param>${customer.name}</@s.param>
            </@s.text>
        </p>
    </div>

    <div style="clear: both;"></div><br/>

    <div class="assetsCount preassignedAssetsCount">
        ${numUnregisteredAssets}
    </div>

    <div class="countExplanation">
        <h2>
            <@s.text name="label.assets_preassigned"/>
        </h2>

        <p>
            <@s.text name="label.assets_preassigned_info">
               <@s.param>${customer.name}</@s.param>
            </@s.text>
        </p>
    </div>

</div>
