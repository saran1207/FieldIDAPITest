<div id="modalBox_message">
	<#if actionErrors.isEmpty() >
		<h3><@s.text name="message.downloadbeinggenerated" /></h3>
		<p><@s.text name="message.download_change_name" /></p>
		<p><span id="label"><@s.text name="label.downloadname"/></span>&nbsp;<span class="egColor"><@s.text name="message.downloadname" /></span></p>
		
		<@s.form id="download_name" action="saveDownloadName" namespace="/" theme="fieldidSimple">	
			<@s.hidden name="fileId" value="${downloadLink.id}"/>		
			<@s.textfield id="reportName" name="reportName" maxlength="255"/>
					
			<div class="formActions">
				<@s.submit id="saveDownload" onClick="Lightview.hide();" key="hbutton.savetomydownloads" style="text-align:left"/>
				&nbsp;<@s.text name="label.or" />&nbsp;
				<a href="javascript:void(0);" onClick="closeLightbox();"><@s.text name="hbutton.saveclosemessage" /></button>
			</div>
		</@s.form>
	<#else>
		<#list actionErrors as error >
			${error}<br/>
		</#list>
		<button style="padding: 2px 5px;" onclick="closeLightbox();"><@s.text name="hbutton.closemessage" /></button>
	</#if>
</div>