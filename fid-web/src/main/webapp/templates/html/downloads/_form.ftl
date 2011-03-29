<span>
	<@s.form id="download_name_${fileId}" action="saveDownloadName" namespace="/ajax" theme="fieldidSimple" >	
		<@s.hidden name="fileId"/>		
		<@s.textfield name="downloadLinkName" value="${downloadLink.name}"/>
		<a id="save_download_name_${fileId}" href="javascript:void(0);" onClick="updateDownloadName( ${fileId} ); return false;"><@s.text name="label.save" /></a>
		|
		<a id="cancel_download_name_${fileId}" href="javascript:void(0);" onClick="cancelDownloadName( ${fileId} ); return false;"><@s.text name="label.cancel" /></a>
		<@s.fielderror>
			<@s.param>downloadLinkName</@s.param>				
		</@s.fielderror>
	</@s.form>			
</span>
