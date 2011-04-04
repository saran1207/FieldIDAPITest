<head>
	<@n4.includeStyle href="downloads" type="page"/>
</head>

<div class="optionsContainer">
	<h1><@s.text name="label.share" />${downloadLink.name}</h1>
	<p class="description"><@s.text name="label.share_this_download" /></p>
	
	<div class="urlContainer">
		${downloadUrl}
	</div>
	
	<div id="copiedConfirmation" class="hidden copiedConfirmation">
		<@s.text name="label.link_copied_confirmation" />
	</div>
	
	<div class="downloadActions">
		<div id="copyLinkButton"><a href=# onclick="$('copiedConfirmation').removeClassName('hidden'); return false;"></a></div>
		<div id="emailLinkButton"><a href='<@s.url action="emailDownloadLinkForm" namespace="/aHtml/iframe" fileId="${fileId}"/>' class='lightview' rel='iframe' title="<@s.text name="label.email_download"/> :: :: scrolling:true, width: 500, height: 500" ></a></div>
	</div>
	
	<div class="warningInformation">
		<@s.text name="label.download_warning" > <span class="bold"><@s.param><@s.text name="label.not"/></@s.param></span><@s.param>${action.getExpiresText(downloadLink.created)}</@s.param></@s.text>
	</div>
</div>