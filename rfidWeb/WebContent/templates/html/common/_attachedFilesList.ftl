<#escape x as x?html >
	<head>
		<link rel="stylesheet" type="text/css" href="<@s.url value="/style/featureStyles/fileUpload.css" />" />
	</head>
	<div id="attachedfiles" >
		<#list attachments as attachedFile >
			<p id="attached_${attachedFile_index}" class="fileUploadShow">
				<#noescape>
				<label id="attached_${attachedFile_index}_label">
					<@s.url id="attachment_url" action="${downloadAction!'downloadAttachedFile'}" namespace="/file" uniqueID="${attachmentID!uniqueID}" fileName="${attachedFile.fileName}" attachmentID="${attachedFile.id}" />
					<#if action.isImage(attachedFile.fileName)>
						<a href="${attachment_url}" target="_blank">
							<img width="150" src="${attachment_url}" alt="${attachedFile.fileName}"/>
						</a>
					<#else>
						<a href="${attachment_url}" target="_blank">${attachedFile.fileName}</a>
					</#if>
						
				</label>
				</#noescape>
				<span>${(attachedFile.comments) !}</span>
			</p>
		</#list>
	</div>
</#escape>