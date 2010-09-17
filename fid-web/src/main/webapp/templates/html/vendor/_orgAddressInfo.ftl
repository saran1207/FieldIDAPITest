<#if org.addressInfo??>
    <p>${org.addressInfo.streetAddress!""}</p>
    <p>${org.addressInfo.city}<#if org.addressInfo.state??>, ${org.addressInfo.state}</#if><#if org.addressInfo.country??>, ${org.addressInfo.country}</#if>
    </p>
    <p>${org.addressInfo.zip!""}</p>
    <p><@s.text name="label.phone"/>: ${org.addressInfo.phone1!""}</p>
    <p><@s.text name="label.fax"/>: ${org.addressInfo.fax1!""}</p>
    <ul>
        <#if org.webSite??>
            <li><a href="<@s.url value="${org.webSite}"/>" target="_blank">${org.webSite}</a></li>
        </#if>
    </ul>
</#if>