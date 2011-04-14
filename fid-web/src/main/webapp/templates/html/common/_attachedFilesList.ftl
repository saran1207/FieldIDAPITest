<#escape x as x?html >
	<head>
		<@n4.includeStyle href="featureStyles/fileUpload" />
	</head>
	<div id="attachedfiles" >
		<#list attachments as attachedFile >
			<p id="attached_${attachedFile_index}" class="fileUploadShow">
				<#noescape>
				<label id="attached_${attachedFile_index}_label"<#if !attachedFile.image>class="defaultImage" </#if>>
					<@s.url id="attachment_url" action="${downloadAction!'downloadAttachedFile'}" namespace="/file" uniqueID="${attachmentID!uniqueID}" fileName="${attachedFile.fileName}" attachmentID="${attachedFile.id}" />
					<#if attachedFile.image>
						<img width="50" src="${attachment_url}" alt="${attachedFile.fileName}"/>
					<#else>
						&nbsp;
					</#if>
				</label>
				<a href="${attachment_url}" target="_blank">${attachedFile.fileName}</a>
				<br/>
				</#noescape>
				<span>${(attachedFile.comments) !}</span>	
			</p>
		</#list>
	</div>
</#escape>