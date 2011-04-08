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
	<div class="downloadBox">
		<div class="boxContent">
			<h2>${publicDownloadLink.name}</h2>
			<p><@s.text name="label.expires"/>:&nbsp;${action.getExpiryDate(publicDownloadLink.created)?datetime}</p>
		</div>
		<div id="downloadButton"><a href="<@s.url action="downloadFile" includeParams="get" downloadId="${downloadId}" />" ></a></div>
	</div>
	<div class="textLink">
		<p><@s.text name="label.if_the_link_above"/></p>
		<p class="coloredLink">${downloadUrl}</p>
	</div>
</div>

