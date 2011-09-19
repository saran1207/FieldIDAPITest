<head>
	<style>	
			body {
				text-align: left;
			}
			.uploadFileForm {
				padding: 20px 10px 0 10px;
				background-color: #F5FBFF;
				height: 80px;
			}			
			.uploadMessage{
				margin-bottom: 5px;
			}
			
			.error {
				display: none;
			}
			
			.errorMessage {
				float: left;
				padding-right: 5px;
			}
			
		</style>
</head>	
<div class="uploadFileForm">
	<@s.form method="POST" action="uploadFile" namespace="/aHtml/fileUploads" theme="simple"  enctype="multipart/form-data">
		<p class="uploadMessage"><@s.text name="message.attachment"/><p>
		<@s.file name="upload" onchange="$('progress').show(); parent.startFileUpload(); this.form.submit(); parent.completedFileUpload();" />
		<@s.hidden name="frameId"/>
		<@s.hidden name="frameCount"/>
		<@s.hidden name="typeOfUpload"/>
		<span id="progress" style="display:none">
			<img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>" alt="<@s.text name="message.uploading"/>"/>
		</span>
		<@s.fielderror ><@s.param>upload</@s.param></@s.fielderror>
		
	</@s.form>
</div>