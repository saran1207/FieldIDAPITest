<title><@s.text name="title.missing_feature"/></title>
<head>
	<style type="text/css">
		.byFeature {
			padding:15px;
			font-size:2em;
			
		}
	</style>
</head>

<div class="byFeature">
	<@s.text name="label.purchase_feature">
		<@s.param>${action.getText(requiredFeature.label)}</@s.param>
	</@s.text>
	<@s.url id="homeUrl" action="home"/>
	
	
</div>
<div class="byFeature">
	<@s.submit theme="fieldidSimple" key="label.back_to_home" onclick="return redirect('${homeUrl}');"/>
</div>