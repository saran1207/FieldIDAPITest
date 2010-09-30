<head>
    <@n4.includeStyle href="regNetworkAsset" type="page"/>
	<#include "/templates/html/common/_lightView.ftl"/>
	
	<script type="text/javascript">
        function redirect(url) {
	        Lightview.hide();
	        window.parent.location=url;
        }
        
        function reload(){
	        Lightview.hide();
	        window.parent.location.reload(true);
        }
	</script>
</head>

<div id="confirm" class="center">
	<div class="confirmBody">
	<p><img src="<@s.url value="/images/register-checkmark.png"/>" /></p>
	<h1>Asset Registered</h1>
	<p><@s.text name="label.registerconfirm"/> - ${linkedProduct.type.name}</p>
	<div id="links">
		<@s.url action="inspectionGroups" uniqueID="${newProduct.id}" namespace="/" id="performFirstEventUrl"/>
		<@s.url action="product" uniqueID="${newProduct.id}" namespace="/" id="viewAssetUrl"/>
		<p><@s.text name="label.whatsnext"/></p>
		<p><a href="#" onclick="redirect('${performFirstEventUrl}');return false;" ><@s.text name="label.performfirstevent"/></a></p>
		<p><a href="#" onclick="redirect('${viewAssetUrl}');return false;"> <@s.text name="label.viewassetinfo"/></a></p>
	</div>
	<p>
		<button onclick="reload();return false;"><@s.text name="label.ok"/></button>
	</p>
	<div>
</div>

