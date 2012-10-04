<head>
	<style>	
		#imageUploaded {
			padding:0px;
			vertical-align:top;
			display:block;
		}
		
		.radio {
			width: 60px;
		}
		
	</style>
	
	<script type="text/javascript">
		countryChangeUrl = "<@s.url action="getRegions" namespace="/public/ajax" />";

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
			var iframe = '<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>';
			$( "imageUploadField" ).insert( { top: iframe } );
			$("imageUploaded").removeClassName( "inputError" );
			$("imageUploaded").title = "";
		}
		
	</script>

    <@n4.includeScript src="timezone.js"/>
</head>

<#include "/templates/html/common/_formErrors.ftl" />
<@s.hidden name="uniqueID"/>

<div class="pageSection" >
	<h2><@s.text name="label.details"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label><@s.text name="indicator.required"/> <@s.text name="label.name"/></label>
			<@s.textfield name="displayName"/>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.name_on_pdf_reports"/></label>
			<@s.textfield name="certificateName"/>
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.country"/></label>
			<@s.select name="countryId" list="countries" listKey="id" listValue="displayName" labelposition="left" cssClass="changesTimeZone"/>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.timezone"/></label>
			<@s.select id="tzlist" name="timeZoneID" list="timeZones" listKey="id" listValue="displayName" labelposition="left" emptyOption="false"/>
		</div>
		
		<#if primary >
			<div class="infoSet">
				<label><@s.text name="label.website_url"/></label>
				<@s.textfield name="webSite"/>
			</div>
		</#if>
	</div>
</div>
<div class="pageSection" >
	<h2><@s.text name="label.address"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label><@s.text name="label.streetaddress"/></label>
			<@s.textfield name="addressInfo.streetAddress"/>
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.city"/></label>
			<@s.textfield name="addressInfo.city"/>
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.state"/></label>
			<@s.textfield name="addressInfo.state"/>
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.country"/></label>
			<@s.textfield name="addressInfo.country"/>
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.zip"/></label>
			<@s.textfield name="addressInfo.zip"/>
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.phonenumber"/></label>
			<@s.textfield name="addressInfo.phone1"/>
		</div>
			
		<div class="infoSet">
			<label><@s.text name="label.fax"/></label>
			<@s.textfield name="addressInfo.fax1"/>
		</div>
	</div>
</div>


<div class="pageSection" >
	<h2><@s.text name="label.pdf_report_image"/></h2>
	<div class="sectionContent">
		<div class="infoSet borderLessInfoSet">
			<label><@s.text name="label.pdf_report_image"/></label> 
			<span id="imageUploadField"  >
				
				<#if !certImageDirectory?exists || certImageDirectory.length() == 0  || removeImage >
					<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />"  scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>
				</#if>
				<span id="imageUploaded" <#if (action.fieldErrors['certImageContentType'])?exists>class="inputError" title="${action.fieldErrors['uploadedImageContentType']}"</#if> <#if  !certImageDirectory?exists || certImageDirectory.length()  == 0  || removeImage >style="display:none;"</#if> >
					<img src="${certImageDirectory!""}" <#if newImage > style="display:none"</#if> id="previewImage" style="width=500;height=200"/>
					
					<@s.hidden name="removeImage" id="removeImage"/>  <a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
					<@s.hidden name="newImage" id="newImage"/>
					<@s.hidden name="certImageDirectory" id="imageDirectory"/>
					
					
				</span>
			</span>
			
		</div>
	</div>
</div>




