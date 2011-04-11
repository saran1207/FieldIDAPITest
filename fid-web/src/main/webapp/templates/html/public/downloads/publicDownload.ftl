<head>
	<title><@s.text name="label.file_download"/></title>
	<@n4.includeStyle href="publicDownload" type="page"/>
	<script type="text/javascript">
		document.observe("dom:loaded", function(){
			$('fieldidLogo').setStyle({
				width: "257px",
				height: "86px"
			});
				$('publicLinkContainer').setStyle({
				display: "block"
			});
		});
	</script>
	<!--[if gt IE 6]>
		<style>
			.publicLinkContainer{
				margin: 23px 0 0 37px;
			}
		</style>
	<![endif]-->
</head>
<div class="downloadContainer">
	<h1><@s.text name="label.your_download_link"/></h1>	
	<@s.url id="actionUrl" action="downloadFile" includeParams="get" downloadId="${downloadId}" />
	<div class="downloadBox">
		<div class="boxContent">
			<h2><a href="${actionUrl}" >${publicDownloadLink.name}</a></h2>
			<p><@s.text name="label.expires"/>:&nbsp;${action.getExpiryDate(publicDownloadLink.created)?datetime}</p>
		</div>
		<div id="downloadButton"><a href="${actionUrl}" ></a></div>
	</div>
	<div class="textLink">
		<p><@s.text name="label.if_the_link_above"/></p>
		<p class="coloredLink">${baseNonFieldIdURI}${actionUrl?substring(1)}</p>
	</div>
</div>

