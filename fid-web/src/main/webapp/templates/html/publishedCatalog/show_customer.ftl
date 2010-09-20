<title><@s.text name="label.safety_network" /></title>
<head>
	<@n4.includeScript>
		var yourDoneUrl = '<@s.url action="safetyNetwork" />';
		var yourCancelUrl = yourDoneUrl;
		var importDoneName;
	</@n4.includeScript>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
	<@n4.includeStyle href="safetyNetworkCustomer" type="page"/>
    <@n4.includeStyle href="vendor" type="page"/>
</head>



<#include '../customer/_customerInfo.ftl'>

<#include "_show.ftl"/>
