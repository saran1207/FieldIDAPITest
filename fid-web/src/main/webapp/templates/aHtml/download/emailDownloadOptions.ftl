<head>
	<@n4.includeStyle href="downloads" type="page"/>
</head>

<div class="optionsContainer">
	<h1><@s.text name="label.share" />${downloadLink.name}</h1>
	<p class="description"><@s.text name="label.share_this_download" /></p>
	<div class="urlContainer">
		${downloadUrl}
	</div>
	
	<div class=" copiedConfirmation">
		<@s.text name="	label.link_copied_confirmation" />
	</div>
	<div id="copyLinkButton"><a href=#></a><div>
	<div id="emailLinkButton"><a href=#></a><div>
	<a href='<@s.url action="emailDownloadLinkForm" namespace="/aHtml/iframe" fileId="${fileId}"/>' class='lightview' rel='iframe' title="<@s.text name="label.email_download"/> :: :: scrolling:true, width: 500, height: 500" ><@s.text name="label.email_download" /></a>
</div>