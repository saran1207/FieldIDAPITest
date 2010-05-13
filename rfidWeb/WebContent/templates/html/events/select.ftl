${action.setPageType('inspection', '')}
<title><@s.text name="label.start_event"/>
<head>
	<@n4.includeStyle type="page" href="events"/>
</head>
<div id="mainContent">
	<ul class="subSystemList">
		<li id="singleEvent">
			<h2 class="clean"><a href="<@s.url action="inspect"/>"><@s.text name="label.inspect_single_asset"/></a></h2>
			<p><a href="<@s.url action="inspect"/>"><@s.text name="label.inspect_single_asset.full"/></a></p>
		</li>
		<li id="multipleEvents">
			<h2 class="clean"><a href="<@s.url action="assetSelection"/>"><@s.text name="label.multi_event"/></a></h2>
			<p><a href="<@s.url action="assetSelection"/>"><@s.text name="label.multi_event.full"/></a></p>
		</li>
		<li id="multipleProofTests">
			<h2 class="clean"><a href="<@s.url action="multiProofTest"/>"><@s.text name="label.multi_proof_test"/></a></h2>
			<p><a href="<@s.url action="multiProofTest"/>"><@s.text name="label.multi_proof_test.full"/></a></p>
		</li>
	</ul>
</div>