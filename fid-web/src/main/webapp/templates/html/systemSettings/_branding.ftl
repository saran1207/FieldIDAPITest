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
					<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="240" height="35" ></iframe>

                    <span id="imageUploaded">
						<@s.hidden name="newImage" id="newImage"/>
						<@s.hidden name="imageDirectory" id="imageDirectory"/>

                        <span id="newImageUploaded" style="display:none"><@s.text name="label.new_image_uploaded" /></span>
						<img src="${action.getMainLogoUrl(tenant.id)}" id="previewImage" target="_blank" width="215" height="61"/>
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

