<head>
	<@n4.includeScript src="jquery.ThreeDots.min.js"/>
	<script type="text/javascript">
			
		function imageFileUploaded( fileName, directory ){
			$("uploadFileForm").removeClassName("uploadFileForm").addClassName("uploadedForm");
			$("assetImageMsg").hide();
			$("imageUpload").remove();
			$("imageUploaded").show();
			$("removeImage").value = "false";
			$("newImage").value = "true";
			$("imageDirectory").value = directory;
			$("newImageLabel").update(fileName);
			var fileext =  fileName.slice(fileName.lastIndexOf('.'))
			jQuery("#imageLabel").ThreeDots({ max_rows:1, whole_word:false, allow_dangle: true, ellipsis_string:'... ' + fileext });
			$("previewImage").setAttribute("src", "images/asset-image-icon.png");
		}
		
		function removeUploadImage() { 
			$( "imageUploaded" ).hide();
			$("removeImage").value = "true";
			var iframe = '<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" typeOfUpload="assetTypeImage"/>" scrolling="no" scrollbar="no" scrolling="no" style="overflow:hidden;" frameborder="0" width="500" height="21" ></iframe>';
			$( "imageUploadField" ).insert( { top: iframe } );
			$("imageUploaded").removeClassName( "inputError" );
			$("imageUploaded").title = "";
            $("imageDirectory").value = "";
            $("newImage").value = "false";
			$("uploadFileForm").removeClassName("uploadedForm").addClassName("uploadFileForm");
			$("imageLabel").removeAttribute("threedots");
			$$("span.threedots_ellipsis").each(function(element) {
					element.remove();
			});
			$("assetImageMsg").show();
		}
				
	</script>
</head>

<div class="assetFormGroup">
	<h2 style="margin-bottom:0;"><@s.text name="label.assetimage"/></h2>
	<div id="uploadFileForm" <#if newImage>class="uploadFileForm"<#else>class="uploadedForm"</#if> >
		<p id="assetImageMsg"  <#if !newImage > style="display:none"</#if>><@s.text name="message.asset_image"/></p>
		<span id="imageUploadField"  >
			<#if !imageDirectory?exists || imageDirectory.length() == 0  || removeImage >
				<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="21" ></iframe>
			</#if>
			<div class="imageUploadPreview" id="imageUploaded" <#if (action.fieldErrors['uploadedImageContentType'])?exists>class="inputError" title="${action.fieldErrors['uploadedImageContentType']}"</#if> <#if  !imageDirectory?exists || imageDirectory.length()  == 0  || removeImage >style="display:none;"</#if> >
				
				<@s.hidden name="newImage" id="newImage"/>
				<@s.hidden name="imageDirectory" id="imageDirectory"/>
				<div class="previewImageDisplay">
					<img src="images/asset-image-icon.png" id="previewImage" alt="uploadedFile"/>
				</div>
				<div class="previewImageLabel" >
					<div id="imageLabel" class="assetImageLabel"> 
						<span id="newImageLabel" class='ellipsis_text'> </span>
					</div>
					
					<script type="text/javascript">
						if($("imageDirectory") != null) {
							var directory = $("imageDirectory").value;
							var filename = directory.slice(directory.indexOf('/') + 1)
							$("newImageLabel").update(filename);
							var fileext =  filename.slice(filename.lastIndexOf('.'));
							jQuery("#imageLabel").ThreeDots({ max_rows:1, whole_word:false, allow_dangle: true, ellipsis_string:'... '+ fileext });
						}
					</script>
				<@s.hidden name="removeImage" id="removeImage"/> 
				<a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
				</div>
			</div>
		</span>
	</div>
</div>
<#assign uploadFileType="assetAttachment"/>
<#include "../common/_attachedFilesForm.ftl" />

