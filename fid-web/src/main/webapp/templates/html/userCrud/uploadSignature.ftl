<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#include "/templates/common/nocacheHeaders.ftl"><html>
	<head>
		<#include "/templates/html/layout/head.ftl">
		<style>
			#imagePreview {
				line-height: 50px;
			}
		</style>
	</head>
	<body style="text-align:left; width: 800px; overflow: hidden;">
		

		<@s.form action="userUploadSignature" cssClass="inputForm"  style="width:780px" theme="simple" enctype="multipart/form-data" method="post">
			<@s.hidden name="uniqueID" />
			<div class="formLabel" style="float:left;width:200px;padding:10px 0px;"><@s.text name="label.signature" />:</div>
				<div style="float:left;padding:10px  0px; width:700px">	
					<@s.fielderror>
						<@s.param>signature</@s.param>				
					</@s.fielderror>
					<@s.file  name="signature"  />
					
					<@s.submit key="label.uploadsignature" />
					<div style="float:right; width: 300px"><@s.text name="label.signatureslookbest"/></div>
					<div  >
						<span id="imagePreview" >
							<img src="<@s.url uniqueID="${uniqueID}" action="userSignature" />" width="203" height="43" alt="<@s.text name="label.nosignature"/>" />
						</span>
						
						<div class="message" id="message">
					          <@s.actionmessage />
					      </div>
					      <div id="error" class="error">
					          <@s.actionerror />
					      </div>
					</div>
				<div>
			
		</@s.form>
		
	</body>
</html>