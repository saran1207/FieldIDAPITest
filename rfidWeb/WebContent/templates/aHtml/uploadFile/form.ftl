<head>
	<style>	
			body {
				text-align: left;
			}
		</style>
</head>	
<#if frameId?exists >
	
	<@s.form method="POST" action="uploadFile" namespace="/aHtml/iframe" theme="simple"  enctype="multipart/form-data" style="height:20px;">
		<@s.file name="upload" onchange="$('progress').show(); parent.startFileUpload(); this.form.submit(); parent.completedFileUpload();" />
		<@s.hidden name="frameId"/>
		<@s.hidden name="frameCount"/>
		<@s.hidden name="typeOfUpload"/>
		<span id="progress" style="display:none">
			<img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>" alt="<@s.text name="message.uploading"/>"/>
		</span>
		<@s.fielderror > <@s.param>upload</@s.param></@s.fielderror>
		
	</@s.form>
<#else>
	<@s.fielderror > <@s.param>upload</@s.param></@s.fielderror>
</#if>	