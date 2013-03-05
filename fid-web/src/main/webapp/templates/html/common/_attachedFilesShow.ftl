<#if !attachments.isEmpty() >
	<h2>
        <@s.text name="label.attachments"/>
        <#if showDownloadAll?exists && showDownloadAll >
            <span style="font-weight: normal;">
                | <#include "_attachedFilesDownloadAll.ftl"/>
            </span>
        </#if>
    </h2>
	<#include "_attachedFilesList.ftl"/>
</#if>