<h1>Asset - ${product.serialNumber}</h1>

<#include "../common/_lightView.ftl" />
<#include '_lightViewBoxOptions.ftl'>

<a class="backToResults" href="<@s.url action="preAssignedAssets" uniqueID="${vendor.id}"/>">&#171; <@s.text name="label.back_to_pre_assigned"/></a>
<p></p><br/>

<#if !action.isProductAlreadyRegistered(product)>
    <div class="registerThisNow">
        <p>
            <a href='<@s.url action="regNetworkProduct.action" namespace="/aHtml/iframe" uniqueID="${product.id}"/>' ${lightViewOptions} ><@s.text name="label.register_this_asset_now"/></a><br/>
            <@s.text name="label.register_explanation"/>
        </p>
    </div>
</#if>

<p></p><br/>