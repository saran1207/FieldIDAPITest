<#if !attachments.isEmpty() >
    <@s.url id="attachment_url" action="${'downloadAllAttachedFiles'}" namespace="/file" uniqueID="${uniqueID}"/>
    <a href="${attachment_url}" target="_blank">Download All</a>
</#if>