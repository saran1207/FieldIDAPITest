<#escape x as x?html >
	<div id="attachedfiles" >
		<#list attachments as attachedFile >
			<br class="smallBr"/>
			<p id="attached_${attachedFile_index}" class="fileUploadShow">
				<#noescape>
				<label id="attached_${attachedFile_index}_label">
					<@s.url id="attachment_url" action="${downloadAction!'downloadAttachedFile'}" namespace="/file" uniqueID="${attachmentID!uniqueID}" fileName="${attachedFile.fileName}" attachmentID="${attachedFile.id}" />
					<#if attachedFile.image>
						<img width="50" src="${attachment_url}" alt="${attachedFile.fileName}"/>
					<#else>
					    <img src="images/default-filetype.png" alt="${attachedFile.fileName}"/>
					</#if>
				</label>
				
				<div class="details">
					<p>
						<a href="${attachment_url}" target="_blank">${attachedFile.fileName}</a>
					</p>
					<p class="attachmentComments">
						${(attachedFile.comments)!}
					</p>
				</div>
				</#noescape>
			</p>
		</#list>
	</div>
</#escape>