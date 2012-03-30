<head>
    <script type="text/javascript">

        jQuery(document).ready(function(){
            jQuery('.registerAssetLightbox').colorbox({iframe: true, width: '770px', height: '550px'});
        });

    </script>
</head>


<h1>Asset - ${asset.identifier}</h1>

<a class="backToResults" href="<@s.url action="preAssignedAssets" uniqueID="${vendor.id}"/>">&#171; <@s.text name="label.back_to_pre_assigned"/></a>
<p></p><br/>

<#if !action.isAssetAlreadyRegistered(asset)>
    <div class="registerThisNow">
        <p>
            <a href='<@s.url action="regNetworkAsset.action" namespace="/aHtml/iframe" uniqueID="${asset.id}"/>' class="registerAssetLightbox" ><@s.text name="label.register_this_asset_now"/></a><br/>
            <@s.text name="label.register_explanation"/>
        </p>
    </div>
</#if>

<p></p><br/>