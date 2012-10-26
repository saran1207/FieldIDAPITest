<head>
    <@n4.includeStyle href="regNetworkAsset" type="page"/>

	<script type="text/javascript">
        function redirect(url) {
	        closeLightbox();
	        window.parent.location=url;
        }
        
        function reload(){
	        closeLightbox();
	        window.parent.location.reload(true);
        }
	</script>
</head>

<div id="confirm" class="center">
	<div class="confirmBody">
	<p><img src="<@s.url value="/images/register-checkmark.png"/>" /></p>
	<h1>Asset Registered</h1>
	<p><@s.text name="label.registerconfirm"/> - ${linkedAsset.type.name}</p>
	<div id="links">
		<@s.url action="quickEvent" assetId="${newAsset.id}" namespace="/" id="performFirstEventUrl"/>
		<@s.url value="/w/assetSummary?uniqueID=${newAsset.id}" namespace="/" id="viewAssetUrl"/>

		<p><@s.text name="label.whatsnext"/></p>
		<p><a id="performFirstEvent" href="#" onclick="redirect('${performFirstEventUrl}');return false;" ><@s.text name="label.performfirstevent"/></a></p>
		<p><a id="viewAsset" href="#" onclick="redirect('${viewAssetUrl}');return false;"> <@s.text name="label.viewassetinfo"/></a></p>
	</div>
	<p>
		<button onclick="reload();return false;"><@s.text name="label.ok"/></button>
	</p>
	<div>
</div>

