<head>
	<style>	
			body {
				text-align: left;
			}

			.uploadFileForm {
				height: 21px;				
			}
			
			.backgroundColor {
				background-color: #F5FBFF;
			}	
		</style>
</head>	
<div class="uploadFileForm <#if backgroundColor>backgroundColor</#if>">
	<@s.form method="POST" action="uploadImage" theme="simple"  enctype="multipart/form-data" style="height:20px;">
		<@s.file name="upload" onchange="$('progress').show(); this.form.submit();" />
		<@s.hidden name="frameId"/>
		<@s.hidden name="frameCount"/>
		<@s.hidden name="typeOfUpload"/>
		<@s.fielderror > <@s.param>upload</@s.param></@s.fielderror>
		<span id="progress" style="display:none">
			<img src="<@s.url value="/images/indicator_mozilla_blu.gif" />" alt="<@s.text name="message.uploading"/>"/>
		</span>
	</@s.form>
</div>
	