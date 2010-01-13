<head>
	<@n4.includeScript src="logoUpload"/>
	<@n4.includeScript>
		uploadImageUrl = '<@s.url action="uploadImageForm" namespace="/ajax"/>';
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
	</style>
</head>


		<div class="infoSet">
			<label for="dateFormat">
				<@s.text name="label.date_format" />
				<a href="javascript:void(0);" id="whatsThis_dateFormat_button" >?</a>
				<div id="whatsThis_dateFormat" class="hidden" style="border :1px solid black"><@s.text name="whatsthis.date_format"/></div>
				<script type="text/javascript">
					$("whatsThis_dateFormat_button").observe( 'click', function(event) { showQuickView('whatsThis_dateFormat', event); } );
				</script>				
			</label>
			<@s.textfield name="dateFormat" theme="fieldidSimple" />
		</div>
<#if securityGuard.brandingEnabled>
		
		<div class="infoSet">
			<label for="systemLogo" ><@s.text name="label.system_logo"/></label>
			<span id="imageUploadField"  >
				<#if !imageDirectory?exists || imageDirectory.length() == 0  || removeImage >
					<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/ajax" />" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>
				</#if>
				<span id="imageUploaded" <#if (action.fieldErrors['uploadedImageContentType'])?exists>class="inputError" title="${action.fieldErrors['uploadedImageContentType']}"</#if> <#if  !imageDirectory?exists || imageDirectory.length()  == 0  || removeImage >style="display:none;"</#if> >
					
					<@s.hidden name="newImage" id="newImage"/>
					<@s.hidden name="imageDirectory" id="imageDirectory"/>
					<span id="newImageUploaded" <#if !newImage >style="display:none"</#if>><@s.text name="label.new_image_uploaded"/> </span> 
					 
					<img src="<@s.url action="downloadTenantLogo"  namespace="/file" uniqueID="${tenant.id}" />" <#if newImage > style="display:none"</#if> id="previewImage" target="_blank" alt="<@s.text name="label.logo_image" />" width="215" height="61"/>
					 
					<@s.hidden name="removeImage" id="removeImage"/> <a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
				</span>
			</span>
			<div id="bestImage">
				<@s.text name="label.account_image_looks_best"/>
			</div>
		</div>
	
		<div class="infoSet">
			<label for="webSite" ><@s.text name="label.website_url"/></label>
			<span class="fieldHolder">
				<@s.textfield name="webSite" theme="fieldidSimple"/> &nbsp;<@s.text name="instruction.url_entry"/>
			</span>
		</div>
		
</#if>