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
		<div class="expiredBoxContent">
			<h1><@s.text name="label.sorry_download_expired"/></h1>
		</div>
	</div>
</div>

