<title>
	<#if uniqueID?exists  >
		${action.setPageType('product_type', 'edit')!}	
	<#else>
		${action.setPageType('product_type', 'add')!}
	</#if>
</title>

<head>
	<style>
		.crudForm p label {
			width:200px;
		}
		
		.crudForm p input[type="text"] {
			width:380px;
		}
		.crudForm p span {
			width:auto;
		}
		
		#imageUploaded {
			padding:0px;
			height:35px;
			display:block;
			width:380px;
		}
		
		
		
	</style>
	
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
			var iframe = '<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/ajax" typeOfUpload="productTypeImage"/>" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>';
			$( "imageUploadField" ).insert( { top: iframe } );
			$("imageUploaded").removeClassName( "inputError" );
			$("imageUploaded").title = "";
		}
		
	</script>
</head>
<@s.form action="productTypeUpdate" theme="simple" cssClass="crudForm largeForm" method="post" onsubmit="return checkForUploads();" >
	<@s.hidden name="uniqueID" />
	<#include "/templates/html/common/_formErrors.ftl" />
	<h2><@s.text name="label.productinformation"/></h2>
	<p>
		<label><@s.text name="label.group"/></label>
		<span>
			<@s.select name="group" list="productTypeGroups" listKey="id" listValue="name" emptyOption="true">
				<#if (action.fieldErrors['group'])?exists> 
					<@s.param name="cssClass">inputError</@s.param>
					<@s.param name="title">${action.fieldErrors['group']}</@s.param>
				</#if>
			</@s.select>
		</span>
	</p>
	<p>
		<label><@s.text name="label.name"/></label>
		<span>
			<@s.textfield name="name">
				<#if (action.fieldErrors['name'])?exists> 
					<@s.param name="cssClass">inputError</@s.param>
					<@s.param name="title">${action.fieldErrors['name']}</@s.param>
				</#if>
			</@s.textfield>
		</span>
	</p>
		
	<p>
		<label><@s.text name="label.warnings"/></label>
		<span>
			<@s.textarea name="warnings"  rows="3" cols="50">
				<#if (action.fieldErrors['warnings'])?exists> 
					<@s.param name="cssClass">inputError</@s.param>
					<@s.param name="title">${action.fieldErrors['warnings']}</@s.param>
				</#if>
			</@s.textarea>
		</span>
	</p>
	<p>
		<label><@s.text name="label.instructions" /></label>
		<span>	
			<@s.textarea name="instructions"  rows="3" cols="50" >
				<#if (action.fieldErrors['instructions'])?exists> 
					<@s.param name="cssClass">inputError</@s.param>
					<@s.param name="title">${action.fieldErrors['instructions']}</@s.param>
				</#if>
			</@s.textarea>
		</span>
	</p>
	
	<p>
		<label><@s.text name="label.cautionsurl" /></label>
		<span>
			<@s.fielderror>
				<@s.param>cautionsUrl</@s.param>				
			</@s.fielderror>
			<@s.textfield name="cautionsUrl" >
				<#if (action.fieldErrors['cautionsUrl'])?exists> 
					<@s.param name="cssClass">inputError</@s.param>
					<@s.param name="title">${action.fieldErrors['cautionsUrl']}</@s.param>
				</#if>
			</@s.textfield>
		</span>
	</p>
	
	
	<p>
		<label><@s.text name="label.hasmanufacturercertificate" /></label>
		<span>
			<@s.checkbox name="hasManufacturerCertificate" />
		</span>
	</p>
	<p>
		<label><@s.text name="label.manufacturercertificatetext" /></label>
		<span>
			<@s.textarea  name="manufacturerCertificateText" rows="5" cols="50" >
				<#if (action.fieldErrors['manufacturerCertificateText'])?exists> 
					<@s.param name="cssClass">inputError</@s.param>
					<@s.param name="title">${action.fieldErrors['manufacturerCertificateText']}</@s.param>
				</#if>
			</@s.textarea>
			
		</span>
	</p>
	<p>
		<label><@s.text name="label.productdescription" /></label>
		<span>
			<@s.textfield name="descriptionTemplate" >
				<#if (action.fieldErrors['descriptionTemplate'])?exists> 
					<@s.param name="cssClass">inputError</@s.param>
					<@s.param name="title">${action.fieldErrors['descriptionTemplate']}</@s.param>
				</#if>
			</@s.textfield>
		</span>
	</p>
	
	<p>
		<label><@s.text name="label.uploadimage"/></label> 
		<span id="imageUploadField"  >
			<#if !productImageDirectory?exists || productImageDirectory.length() == 0  || removeImage >
				<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/ajax" typeOfUpload="productTypeImage"/>" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>
			</#if>
			<span id="imageUploaded" <#if (action.fieldErrors['uploadedImageContentType'])?exists>class="inputError" title="${action.fieldErrors['uploadedImageContentType']}"</#if> <#if  !productImageDirectory?exists || productImageDirectory.length()  == 0  || removeImage >style="display:none;"</#if> >
				<@s.url id="previewImage" includeParams="none" uniqueID="${uniqueID!}" action="viewProductTypeImage" />
				<a href="${previewImage}" <#if newImage > style="display:none"</#if> id="previewImage" target="_blank" onclick="window.open('${previewImage}', '_blank', 'width=500,height=300'); return false;" ><@s.text name="label.viewimage" /></a>  
				<@s.hidden name="removeImage" id="removeImage"/> <a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
				<@s.hidden name="newImage" id="newImage"/>
				<@s.hidden name="productImageDirectory" id="imageDirectory"/>
			</span>
		</span>
		<div id="bestImage">
			
			<@s.text name="label.productimageslookbest"/>
		</div>
	</p>
	
	<h2><@s.text name="label.attributes"/></h2>
	<p>
		
		
		<#include "_infoFields.ftl"/>
		
	</p>
	
	<#include "../common/_attachedFilesForm.ftl"/>
	<script type="text/javascript">uploadFileLimit = ${fileUploadMax};</script>
	<div class="formAction">
		
		<#if uniqueID?exists  >
			<input type="button" value="<@s.text name="label.delete" />" onclick="return redirect('<@s.url action="productTypeConfirmDelete" uniqueID="${uniqueID}"/>');" /> 
			<input type="button" value="<@s.text name="label.cancel" />" onclick="return redirect('<@s.url action="productType" uniqueID="${uniqueID}"/>');" />
		<#else>
			<input type="button" value="<@s.text name="label.cancel" />" onclick="return redirect('<@s.url action="productTypes"/>');" />
		</#if>
		<@s.submit key="label.save" onclick="formSubmit();" />
		
	</div>
	
	
</@s.form>
