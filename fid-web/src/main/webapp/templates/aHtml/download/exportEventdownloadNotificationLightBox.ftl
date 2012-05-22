<div id="modalBox_message">
		<h3><@s.text name="message.downloadbeinggenerated" /></h3>
		<p><@s.text name="message.download_change_name" /></p>
		<p><span id="label"><@s.text name="label.downloadname"/></span>&nbsp;<span class="egColor"><@s.text name="message.downloadname" /></span></p>
		
		<@s.form id="download_name" action="saveEventExportDownloadName" namespace="/" theme="fieldidSimple">	
			<@s.hidden name="linkId" value="${downloadLink.id}"/>
			<@s.textfield id="reportName" name="reportName" maxlength="255"/>
					
			<div class="formActions">
				<@s.submit id="saveDownload" onClick="closeLightbox();" key="hbutton.savetomydownloads" style="text-align:left"/>
				&nbsp;<@s.text name="label.or" />&nbsp;
				<a href="javascript:void(0);" onClick="closeLightbox();"><@s.text name="hbutton.saveclosemessage" /></a>
			</div>
		</@s.form>
</div>