<#assign newSignatureSection>
    <#include "../eventCrud/_signatureCriteriaResultEdit.ftl"/>
</#assign>

<#escape x as x?js_string>
   performThumbnailRefresh('${newSignatureSection}');
   jQuery('.signatureCriteriaLightBox').colorbox( {iframe: true, scrolling: false, width: '820px', height: '360px'});
</#escape>