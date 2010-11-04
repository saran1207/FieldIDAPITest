<title><@s.text name="label.start_event"/>
<head>
	<@n4.includeStyle type="page" href="events"/>
</head>
<ul class="subSystemList separated">
	<li id="singleEvent">
		<h2 class="clean"><a href="<@s.url action="selectEventAsset"/>"><@s.text name="label.perform_event_single_asset"/></a></h2>
		<p><a href="<@s.url action="selectEventAsset"/>"><@s.text name="label.perform_event_single_asset.full"/></a></p>
	</li>
	<#if sessionUser.hasAccess("createevent") == true >
		<li id="multipleEvents">
			<h2 class="clean"><a href="<@s.url action="assetSelection"/>"><@s.text name="label.multi_event"/></a></h2>
			<p><a href="<@s.url action="assetSelection"/>"><@s.text name="label.multi_event.full"><@s.param>${maxAssetsFromMassEvent!250}</@s.param></@s.text></a></p>
		</li>
		<li id="multipleProofTests">
			<h2 class="clean"><a href="<@s.url action="multiProofTest"/>"><@s.text name="label.multi_proof_test"/></a></h2>
			<p><a href="<@s.url action="multiProofTest"/>"><@s.text name="label.multi_proof_test.full"/></a></p>
		</li>
	</#if>
</ul>
