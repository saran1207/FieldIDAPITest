<#if org.addressInfo??>
    <p class="notranslate">${org.addressInfo.streetAddress!""}</p>
    <p class="notranslate"><#if org.addressInfo.city??>${org.addressInfo.city}</#if><#if org.addressInfo.state??>, ${org.addressInfo.state}</#if><#if org.addressInfo.country??>, ${org.addressInfo.country}</#if>
    </p>
    <p class="notranslate">${org.addressInfo.zip!""}</p>
    <p class="notranslate"><@s.text name="label.phone"/>: ${org.addressInfo.phone1!""}</p>
    <p class="notranslate"><@s.text name="label.fax"/>: ${org.addressInfo.fax1!""}</p>
    <ul class="notranslate">
        <#if org.webSite??>
            <li><a href="<@s.url value="${org.webSite}"/>" target="_blank">${org.webSite}</a></li>
        </#if>
    </ul>
</#if>