<head>
	<@n4.includeStyle href="downloads" type="page"/>
    <script type="text/javascript">

        jQuery(document).ready(function(){
            jQuery('#emailLink').colorbox({title: '<@s.text name="label.email_download"/>', width: '500px', height: '500px'});
        });

    </script>
</head>

<div class="optionsContainer">
	<h1><@s.text name="label.share" /> "${downloadLink.name}"</h1>
	<p class="description"><@s.text name="label.share_this_download" /></p>
	
	<div class="urlContainer">
		${downloadUrl}
	</div>
	
	<div id="emailLinkButton">
        <a id="emailLink" href='<@s.url action="emailDownloadLinkForm" namespace="/aHtml/iframe" fileId="${fileId}"/>'> </a>
    </div>

	
	<div class="warningInformation">
		<@s.text name="label.download_warning" > <@s.param><span class="bold"><@s.text name="label.not"/></span></@s.param><@s.param>${action.getExpiresText(downloadLink.created)?lower_case}</@s.param></@s.text>
	</div>
</div>