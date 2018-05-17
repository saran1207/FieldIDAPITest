<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="vendor" type="page"/>
        <script type="text/javascript">

            jQuery(document).ready(function(){
                jQuery('#videoLighbox').colorbox({iframe: true, width: '900px', height: '570px'});
            });

        </script>
</head>

<#include '_vendorinfo.ftl'>

<div id="mainContent" class="notranslate">
	<#include "../common/_formErrors.ftl"/>
	<h1 class="safetyNetworkHeadings notranslate">${vendor.name}<h1>
	<p class="vendorConnection"><@s.text name="label.vendor_connection"/></p>
	<div id="registerAssets">
		<h3 class="vendorHeadings">
			<@s.text name="label.register_assets">
				<@s.param>${vendor.name}</@s.param>
			</@s.text>
		</h3>
			<p class="vendorParagraph">
				<@s.text name="label.register_assets.msg"/>
				<a id="videoLighbox" href='/videos/instructions/safetynetwork/register.html'><@s.text name="label.tell_me_more"/></a>
				<@s.form action="searchNetworkAsset" theme="fieldid" cssClass="fullForm" method="get">
					<@s.textfield name="searchText" id="assetSearchBox" cssClass="inputAlign"/>
					<@s.hidden name="vendorId" value="%{vendor.id}" />
					<@s.submit key="hbutton.search" id="searchForAssetButton" cssClass="saveButton save inputAlign" />
				</@s.form>
			</p>
	</div>
	<div id="preAssignedAssets">
		<h3 class="vendorHeadings">
			<@s.text name="label.pre_assigned_assets">
				<@s.param>${vendor.name}</@s.param>
			</@s.text>
		</h3>
		<p class="vendorParagraph">
			<@s.text name="label.pre_assigned_assets.msg"/>
			<a class="vendorLink" href="<@s.url value="preAssignedAssets.action" uniqueID="${vendor.id}" />" > <@s.text name="label.view_pre_assigned_asset"/></a>
		</p>
	</div>
</div>
