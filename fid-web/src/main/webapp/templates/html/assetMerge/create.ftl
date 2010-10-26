<title><@s.text name="title.merge_assets"/> - ${losingAsset.serialNumber?html} <@s.text name="label.into"/> ${winningAsset.serialNumber?html}</title>
<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/steps.css"/>" />
</head>

<div class="step">
	<h2><@s.text name="label.you_are_done"/></h2>
	<div class="stepContent">
		<p>
			<@s.text name="message.assets_being_merged"/>
		</p>
		<p>
			<@s.text name="label.go_to_winning_asset_page"/> <a href="<@s.url action="asset" uniqueID="${winningAsset.id}"/>">${winningAsset.serialNumber?html}</a>
		</p>	
	</div>
</div>