<#assign newSignatureSection>
    <#include "../eventCrud/_signatureCriteriaResultEdit.ftl"/>
</#assign>

<#escape x as x?js_string>
   performThumbnailRefresh('${newSignatureSection}');
</#escape>