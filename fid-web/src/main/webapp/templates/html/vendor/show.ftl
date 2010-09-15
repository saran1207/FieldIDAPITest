<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>

<#include '_vendorinfo.ftl'>

<div id="mainContent">
	<#include "../common/_formErrors.ftl"/>
	<h1 class="safetyNetworkHeadings">${vendor.name}<h1>
	<p><@s.text name="label.vendor_connection"/></p>
	<div id="registerAssets">
		<h3>
			<@s.text name="label.register_assets">
				<@s.param>${vendor.name}</@s.param>
			</@s.text>
		</h3>
			<p>	
				<@s.text name="label.register_assets.msg"/><a>&nbsp;<@s.text name="label.tell_me_more"/></a>
				<@s.form action="searchNetworkProduct" theme="fieldid" cssClass="fullForm" method="get">
					<@s.textfield name="searchText" id="assetSearchBox" cssClass="inputAlign"/>
					<@s.hidden name="vendorId" value="%{vendor.id}" />
					<@s.submit key="hbutton.search" id="searchForAssetButton" cssClass="saveButton save inputAlign" />
				</@s.form>
			</p>
	</div>
	<div id="preAssignedAssets">
		<h3>
			<@s.text name="label.pre_assigned_assets">
				<@s.param>${vendor.name}</@s.param>
			</@s.text>
		</h3>
		<p>
			<@s.text name="label.pre_assigned_assets.msg"/>
			<a href="<@s.url value="preAssignedAssets.action" uniqueID="${vendor.id}" />" > <@s.text name="label.view_pre_assigned_asset"/></a>
		</p>
	</div>
</div>
