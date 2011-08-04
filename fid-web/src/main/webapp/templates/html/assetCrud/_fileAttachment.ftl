<head>
	<script type="text/javascript">
			
		function imageFileUploaded( fileName, directory ){
			$("imageUpload").remove();
			$("imageUploaded").show();
			$("removeImage").value = "false";
			$("previewImage").hide();
			$("newImage").value = "true";
			$("imageDirectory").value = directory;
		}
		
		function removeUploadImage() { 
			$( "imageUploaded" ).hide();
			$("removeImage").value = "true";
			var iframe = '<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" typeOfUpload="assetTypeImage"/>" scrolling="no" scrollbar="no" scrolling="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>';
			$( "imageUploadField" ).insert( { top: iframe } );
			$("imageUploaded").removeClassName( "inputError" );
			$("imageUploaded").title = "";
            $("imageDirectory").value = "";
            $("newImage").value = "false";
		}
		
	</script>
</head>

<div class="assetFormGroup">
	<h2><@s.text name="label.assetimage"/></h2>
	<div class="uploadFileForm">
		<p><@s.text name="message.asset_image"/></p>
		<span id="imageUploadField"  >
			<#if !imageDirectory?exists || imageDirectory.length() == 0  || removeImage >
				<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>
			</#if>
			<span id="imageUploaded" <#if (action.fieldErrors['uploadedImageContentType'])?exists>class="inputError" title="${action.fieldErrors['uploadedImageContentType']}"</#if> <#if  !imageDirectory?exists || imageDirectory.length()  == 0  || removeImage >style="display:none;"</#if> >
				
				<@s.hidden name="newImage" id="newImage"/>
				<@s.hidden name="imageDirectory" id="imageDirectory"/>
				<span id="newImageUploaded" <#if !newImage >style="display:none"</#if>><@s.text name="label.new_image_uploaded"/> </span> 
				<#if !newImage>
					<img src="<@s.url action="downloadAssetImage"  namespace="/file" uniqueID="${asset.uniqueID!}" />" <#if newImage > style="display:none"</#if> id="previewImage" target="_blank" alt="<@s.text name="label.logo_image" />" width="50"/>
				</#if>
				<@s.hidden name="removeImage" id="removeImage"/> <a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
			</span>
		</span>
	</div>
</div>
<#assign uploadFileType="assetAttachment"/>
<#include "../common/_attachedFilesForm.ftl" />

