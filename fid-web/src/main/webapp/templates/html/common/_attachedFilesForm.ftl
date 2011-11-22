<head>
	<script type="text/javascript" src="<@s.url value="/javascript/fileUpload.js"/>" ></script>
	<@n4.includeStyle href="featureStyles/fileUpload" />
	<script type="text/javascript">
		removeText = '<@s.text name="label.remove"/>';
		commentsText = '<@s.text name="label.comments"/>';
		uploadUrl = '<@s.url action="uploadForm" namespace="/aHtml/fileUploads"/>';
		uploadWarning = "<@s.text name="warning.filesstilluploading"/>";
		frameCount = ${(uploadedFiles?size)!0};
		uploadFileLimit = ${fileUploadMax};
		tooManyFileMessage = "<@s.text name="warning.max_files_uploaded"><@s.param>${fileUploadMax}</@s.param></@s.text>";
		hasUploadForm = false;
	</script>
	<@n4.includeScript src="jquery.ThreeDots.min.js"/>
</head>


	<div id="attachments" class="assetFormGroup">
		<h2><@s.text name="label.attachments"/></h2>
		<#list attachments as attachedFile >
			<#if attachedFile?exists>
				
				<div id="attached_${attachedFile_index}" class="fileUpload assetUploadPreview attachementsPreview">
					<@s.hidden id="attachedFile_${attachedFile_index}" name="attachments[${attachedFile_index}].id" />
					<@s.hidden id="attachedFile_${attachedFile_index}" name="attachments[${attachedFile_index}].fileName" />
					
				<div class="previewImageDisplay">
					<img src="images/attachment-icon.png" class="previewImage" alt="uploadedFile"/>
				</div>
					<div class="previewImageLabel" >
						<div id="imageLabel" class="attachmentLabel"> 
							<span id="attached_${attachedFile_index}_label" class='ellipsis_text'>${attachments[attachedFile_index].fileName}</span>
						</div>
						<script type="text/javascript">						
							var filename =  "${attachments[attachedFile_index].fileName}";
							var ext =  filename.slice(filename.lastIndexOf('.'));
							/*jQuery(".attachmentLabel").ThreeDots({ max_rows:1, whole_word:false, allow_dangle: true, ellipsis_string:'... '+ ext });*/
						</script>
						<a href="javascript:void(0)"  onclick="$('attached_${attachedFile_index}').remove();addUploadFile('${uploadFileType!}', true);return false;"><@s.text name="label.remove"/></a>
					</div>
					<div class="commentContainer">
						<@s.textarea name="attachments[${attachedFile_index}].comments" id="attachments[${attachedFile_index}].comments"  cols="50" rows="3" theme="fieldidSimple"/>
					</div>
				</div>
				
			</#if>
		</#list>
	</div>
<div class="uploaded">
	<div id="uploadedfiles" class="uploadedfiles" 
			<#if (action.fieldErrors['uploadedFiles'])?exists> 
				class="inputError"
			</#if>
			<#if (action.fieldErrors['uploadedFiles'])?exists> 
				title="${action.fieldErrors['uploadedFiles']}"
			</#if> 
		>  
		<#list uploadedFiles as uploadedFile >
			<#if uploadedFile?exists>
				<div id="frame_${uploadedFile_index}" class="fileUpload infoSet">
					<@s.hidden name="uploadedFiles[${uploadedFile_index}].fileName" />
					<label>${action.getFileName(uploadedFile.fileName)}</label>
					<a href="javascript:void(0)" onclick="$('frame_${uploadedFile_index}').remove();addUploadFile('${uploadFileType!}', true);return false;"><@s.text name="label.remove"/></a>
					<div>
						<@s.textarea name="uploadedFiles[${uploadedFile_index}].comments" id="uploadedFiles[${uploadedFile_index}].comments"  cols="50" rows="3" theme="fieldidSimple"/>
					</div>
				</div>
				
			</#if>
		</#list>
	</div>
</div>

<div class="uploadAction">
	<script type="text/javascript">
		addUploadFile('${uploadFileType!}', false);
	</script>
</div>
