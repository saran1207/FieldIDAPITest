<title>
	<#if uniqueID?exists  >
		${action.setPageType('asset_type', 'edit')!}
	<#else>
		${action.setPageType('asset_type', 'add')!}
	</#if>
</title>

<head>
	<@n4.includeScript src="jquery-1.4.2.min.js"/>
	<@n4.includeScript src="jquery-ui-1.8.13.custom.min"/>	
	<@n4.includeScript>jQuery.noConflict();</@n4.includeScript>
	<@n4.includeStyle href="jquery-redmond/jquery-ui-1.8.13.custom"/>
	
		

	<style>	
		.crudForm .infoSet textarea, .crudForm .infoSet input[type="text"] {
    		width: 380px;
		}
		
		.crudForm .infoSet .infoFieldCol select{
		    width: 150px;
		}
		
		.crudForm .infoSet .infoFieldCol input[type="text"] {
			width: 200px;
		}
		
		.crudForm .infoSet .infoFieldCol label {
			width: 130px;
			float: right;
			padding: 0;
		}
		#imageUploaded {
			padding:0px;
			height:35px;
			display:block;
			width:380px;
		}
		
		#bestImage{
			padding: 5px 5px 5px 185px;
		}
		
		.crudForm .label {
 		   font-weight: normal;
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
			var iframe = '<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" typeOfUpload="assetTypeImage"/>" scrolling="no" scrollbar="no" scrolling="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>';
			$( "imageUploadField" ).insert( { top: iframe } );
			$("imageUploaded").removeClassName( "inputError" );
			$("imageUploaded").title = "";
            $("imageDirectory").value = "";
            $("newImage").value = "false";
		}

		function showTemplateHelp(id, element) {
			var locator = '#'+id;
			jQuery(locator).dialog('open');		
		}
		
		function initTemplateHelp(id) {
		    jQuery('#'+id).dialog({autoOpen:false,width:420});
  		}
		
	</script>
</head>
<@s.form action="assetTypeUpdate" id="assetTypeUpdate" theme="fieldidSimple" cssClass="crudForm largeForm" method="post" onsubmit="return checkForUploads();" >
	<@s.hidden name="uniqueID" />
	<#include "/templates/html/common/_formErrors.ftl" />
	<h2><@s.text name="label.assetinformation"/></h2>
	<div class="infoSet">
		<label class="label"><@s.text name="label.group"/></label>
		<span class="fieldHolder">
			<@s.select name="group" list="assetTypeGroups" listKey="id" listValue="name" emptyOption="true"/>
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.name"/></label>
		<span class="fieldHolder">
			<@s.textfield name="name"/>				
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.warnings"/></label>
		<span class="fieldHolder">
			<@s.textarea name="warnings"  rows="3" cols="50"/>
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.more_information" /></label>
		<span class="fieldHolder">	
			<@s.textarea name="instructions"  rows="3" cols="50" />
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.cautionsurl" /></label>
		<span class="fieldHolder">
			<@s.textfield name="cautionsUrl" />
		</span>
	</div>
	<#if securityGuard.manufacturerCertificateEnabled>
		<div class="infoSet">
			<label class="label"><@s.text name="label.hasmanufacturercertificate" /></label>
			<span class="fieldHolder">
				<@s.checkbox name="hasManufacturerCertificate" />
			</span>
		</div>
		<div class="infoSet">
			<label class="label"><@s.text name="label.manufacturercertificatetext" /></label>
			<span class="fieldHolder">
				<@s.textarea  name="manufacturerCertificateText" rows="5" cols="50" />
			</span>
		</div>
	</#if>
	<div class="infoSet">
		<label class="label"><@s.text name="label.assetdescription" /></label>
		<span class="fieldHolder">
			<@s.textfield name="descriptionTemplate"/>											
		</span>
  	 	<a class="help-logo" href="#" onclick="showTemplateHelp('templateDialog', this);return false;"></a>
		<div id="templateDialog" class="help-dialog">		
					 
			<h2 style="padding:5px 0px;">Asset Description Template Formatting</h2>
			<p>
			Asset Description template is used throughout Field ID Web &amp; Mobile to provide a description of each asset.&nbsp; You can customize how this looks for each Asset Type.&nbsp; Sentences are created with a combination of preconfigured and custom notations.&nbsp; <br>
	
			<br>
			There is no limit to the number of notations you can use and each notation can be separated by any character or word. <br>
			<br>
			</p>
			
			<span style="font-weight: bold;"> </span>
			<div style="line-height:150%;">
			<table class="Default" style="width: 400px;">
				<tbody>
					<tr>
						<td >
							<span style="font-weight: bold;">Notation</span><br>
			
							</td>
						<td >
							<span style="font-weight: bold;">Displays</span><br>
							</td>
					</tr>
					<tr>
						<td >{Identifier} <br>
							</td>
			
						<td>The Identifier of the asset<br>
							</td>
					</tr>
					<tr>
						<td >{RFID}<br>
							</td>
						<td >The RFID Number on the asset<br>
			
							</td>
					</tr>
					<tr>
						<td >{RefNumber}<br>
							</td>
						<td >The Reference Number on the asset<br>
							</td>
					</tr>
			
					<tr>
						<td >{OrderNumber}<br>
							</td>
						<td >The Order Number on the asset<br>
							</td>
					</tr>
					<tr>
						<td >{PONumber}<br>
			
							</td>
						<td class="alt">The Purchase Order on the asset<br>
							</td>
					</tr>
					<tr>
						<td >{<i>CustomAttributeName</i>}</td>			
						<td >The value of the attribute specified<br></td>
					</tr>
				</tbody>
			</table>
			
			

			<p><br><b>Examples</b></p>

			<div style="border-style:solid;border-color:#DDD;border-width:1px;padding:3px;margin:3px 0px;">			
			{Identifier} ({RFID}), {Size} X {Length}, {Type}, {WorkingLoadLimit}
			</div>

			<p style="color:#999">	will display:</p>
			<p>SN421 (E00f01000089EE5B8), 1/4" X 42ft, BD2, 24,000lbs</p>
			
			<br>
			
			<div style="border-style:solid;border-color:#DDD;border-width:1px;padding:3px;margin:3px 0px;">			
				Manufactured by {Manufacturer} on {Birthdate} with a 2 year warranty
			</div>
			<p style="color:#999">will display :</p>			
			<p>Manufactured by ACME on 12/12/11 with a 2 year warranty</p>		

			<br>
		</div>
		
		</div>
		<script type="text/javascript">
			initTemplateHelp('templateDialog');
		</script>		
	</div>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.uploadimage"/></label> 
		<span id="imageUploadField"  >
			<#if !assetImageDirectory?exists || assetImageDirectory.length() == 0  || removeImage >
				<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" typeOfUpload="assetTypeImage"/>" scrolling="no" scrollbar="no" scrolling="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>
			</#if>
			<span id="imageUploaded" <#if (action.fieldErrors['uploadedImageContentType'])?exists>class="inputError" title="${action.fieldErrors['uploadedImageContentType']}"</#if> <#if  !assetImageDirectory?exists || assetImageDirectory.length()  == 0  || removeImage >style="display:none;"</#if> >
				<@s.url id="previewImage" uniqueID="${uniqueID!}" namespace="/file" action="downloadAssetTypeImage" />
				<a href="${previewImage}" <#if newImage > style="display:none"</#if> id="previewImage" target="_blank" onclick="window.open('${previewImage}', '_blank', 'width=500,height=300'); return false;" ><@s.text name="label.viewimage" /></a>  
				<@s.hidden name="removeImage" id="removeImage"/> <a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
				<@s.hidden name="newImage" id="newImage"/>
				<@s.hidden name="assetImageDirectory" id="imageDirectory"/>
			</span>
		</span>
		<div id="bestImage">
			<@s.text name="label.assetimageslookbest"/>
		</div>
	</div>
	
	<h2><@s.text name="label.attributes"/></h2>
	<div class="infoSet">
		<#include "_infoFields.ftl"/>
	</div>
	
	<#include "../common/_attachedFilesForm.ftl"/>
	<div class="formAction">
		
		<@s.submit key="label.save" onclick="formSubmit();" id="save"/>
		<@s.text name="label.or"/>
		
		<#if uniqueID?exists  >
			<a href="#" onclick="return redirect('<@s.url action="assetType" uniqueID="${uniqueID}"/>');" /><@s.text name="label.cancel" /></a>
			<@s.text name="label.or"/>
			<a href="#" onclick="return redirect('<@s.url action="assetTypeConfirmDelete" uniqueID="${uniqueID}"/>');" /><@s.text name="label.delete" /></a>
		<#else>
			<a href="#" onclick="return redirect('<@s.url action="assetTypes"/>');" /><@s.text name="label.cancel" /></a>
		</#if>
		
	</div>
	
</@s.form>
