<head>
</head>
<div id="attachedfiles" >
    <#list attachments as attachedFile >
        <div class="fileUploadShow">
            <div class="attachmentThumb">
                <@s.url id="attachment_url" action="${downloadAction!'downloadAttachedFile'}" namespace="/file" uniqueID="${uniqueID}" attachmentID="${attachedFile.id}" />
                <@s.url id="document_icon_url" value="/images/document-icon.png"/>
                <#if attachedFile.image>
                    <#assign preview_image = attachment_url/>
                    <#assign image_width = '63'/>
                <#else>
                    <#assign preview_image = document_icon_url/>
                    <#assign image_width = '55'/>
                </#if>
                <a href="${attachment_url}" target="_blank">
                    <img class="additionalInfoImage" width="${image_width}" height="65" src="${preview_image}"/>
                </a>
            </div>
            <div class="attachmentText">
                <a href="${attachment_url}" target="_blank">${attachedFile.fileName}</a><br/>
                ${(attachedFile.comments) !}
            </div>
        </div>
    </#list>
</div>