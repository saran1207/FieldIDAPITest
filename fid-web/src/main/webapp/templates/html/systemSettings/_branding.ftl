<head>
	<@n4.includeScript src="logoUpload"/>
	<@n4.includeScript>
		uploadImageUrl = '<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads"/>';
	</@n4.includeScript>
	<style type="text/css">
		#previewImage {
			vertical-align: middle; 
		}
		
		#bestImage {
			margin-left: 185px;
			clear:both;
		}
		#newImageUploaded { 
			display:inline;
			width:auto;
			padding:0;
			padding-right:1em;
		}
		
		.infoSet p{
			display: none;
		}
	</style>
</head>

	<div class="sectionContent">
		<div class="infoSet">
			<label class="label hideInQuickSetup" for="systemLogo" ><@s.text name="label.system_logo"/></label>
			<span class="fieldHolder">
				<span class="weak hideInQuickSetup">
					<@s.text name="label.account_image_looks_best"/>
				</span><br/>
				<span id="imageUploadField"  >
					<#if !imageDirectory?exists || imageDirectory.length() == 0  || removeImage >
						<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>
					</#if>
					<span id="imageUploaded" <#if (action.fieldErrors['uploadedImageContentType'])?exists>class="inputError" title="${action.fieldErrors['uploadedImageContentType']}"</#if> <#if  !imageDirectory?exists || imageDirectory.length()  == 0  || removeImage >style="display:none;"</#if> >
						
						<@s.hidden name="newImage" id="newImage"/>
						<@s.hidden name="imageDirectory" id="imageDirectory"/>
						<span id="newImageUploaded" <#if !newImage >style="display:none"</#if>><@s.text name="label.new_image_uploaded"/> </span> 
						 
						<img src="<@s.url action="downloadTenantLogo"  namespace="/file" uniqueID="${tenant.id}" />" <#if newImage > style="display:none"</#if> id="previewImage" target="_blank" alt="<@s.text name="label.logo_image" />" width="215" height="61"/>
						 
						<@s.hidden name="removeImage" id="removeImage"/> <a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
					</span>
				</span>
			</span>
		</div>
	
		<div class="infoSet">
			<label class="label hideInQuickSetup" for="webSite" ><@s.text name="label.website_url"/></label>
			<p><@s.text name="label.enter_your_corporate_website_address"/></p>
			<span class="fieldHolder">
				<@s.textfield name="webSite" theme="fieldidSimple"/> &nbsp;<@s.text name="instruction.url_entry"/>
			</span>
		</div>
	</div>

