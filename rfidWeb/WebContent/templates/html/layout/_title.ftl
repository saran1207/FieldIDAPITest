<#if title?exists && title?length gt 0 >
	${title}
<#else>
    <#if navOptions.useEntityTitle()>
    	<@s.text name="${navOptions.title}.singular"/> <#if navOptions.entityIdentifier?exists >- ${("("+navOptions.entityIdentifier+"?html)!")?eval}</#if>
    <#else>
    	<@s.text name="${navOptions.title}.plural"/>
    </#if>
 </#if>