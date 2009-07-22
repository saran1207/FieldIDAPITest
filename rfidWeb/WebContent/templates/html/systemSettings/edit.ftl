
<head>
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
	<script type="text/javascript">
		function imageFileUploaded( fileName, directory ){
			$("imageUpload").remove();
			$("imageUploaded").show();
			$("removeImage").value = "false";
			$("previewImage").hide();
			$("newImage").value = "true";
			$("newImageUploaded").show();
			$("imageDirectory").value = directory;
		}
		
		function removeUploadImage() { 
			$( "imageUploaded" ).hide();
			$("removeImage").value = "true";
			var iframe = '<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/ajax"/>" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>';
			$( "imageUploadField" ).insert( { top: iframe } );
			$("imageUploaded").removeClassName( "inputError" );
			$("imageUploaded").title = "";
		}
		
	</script>
</head>

${action.setPageType('account_settings', 'show')!}

<div class="pageSection crudForm largeForm">
	<h2><@s.text name="label.about_your_system"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label  for="companyID"><@s.text name="label.companyid"/></label>
			<span class="fieldHolder">${sessionUser.tenant.name?html}</span>			
		</div>
		<div class="infoSet">
			<label ><@s.text name="label.company_name"/></label>
			<span class="fieldHolder">${sessionUser.tenant.displayName?html}</span>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.login_url"/></label>
			<span class="fieldHolder">${loginUrl?html}</span>
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.disk_usage"/></label>
			<div class="fieldHolder" style="float:left">
				
				<div style="width:300px"><@n4.percentbar total="1000" progress="400"/></div>
				<div >400 of 1000</div>
			</div>
		</div>
		
		<div class="infoSet">
			<label >
				<@s.text name="label.embedded_login_snipit"/>
				<a href="javascript:void(0);" id="whatsThis_reportTitle_button" >?</a>
				<div id="whatsThis_embededLoginCode" class="hidden" style="border :1px solid black"><@s.text name="whatsthis.embedded_login_code"/></div>
				<script type="text/javascript">
					$("whatsThis_reportTitle_button").observe( 'click', function(event) { showQuickView('whatsThis_embededLoginCode', event); } );
				</script>
			</label>
			<span class="fieldHolder">
				<#assign snipit><iframe src="${embeddedLoginUrl}" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="300" ></iframe></#assign>
				${snipit?html}
			</span>
	</div>
</div>
<#if securityGuard.brandingEnabled>
	<@s.form action="systemSettingsUpdate" cssClass="crudForm pageSection largeForm" theme="fieldid">
		<#include "../common/_formErrors.ftl"/>
		<h2><@s.text name="label.branding_settings"/></h2>
		
		
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
		
		<div class="formAction">
			<@s.submit key="label.save"/> <@s.text name="label.or"/> <a href="<@s.url action="administration"/>"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</#if>
