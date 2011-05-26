${action.setPageType('my_account', 'downloads')!}

<head>
	<#if fileId?exists>
		<meta http-equiv="refresh" content="0;url=<@s.url action="downloadFile" includeParams="get" />"></meta>
	</#if>
	<@n4.includeStyle href="downloads" type="page"/>
	<@n4.includeScript src="downloads.js" />
	<script type="text/javascript">
		editDownloadNameUrl = '<@s.url action="editDownloadName" namespace="/ajax"/>'
		saveDownloadNameUrl = '<@s.url action="saveDownloadName" namespace="/ajax"/>'
		cancelDownloadNameUrl = '<@s.url action="cancelDownloadName" namespace="/ajax"/>'
	</script>
</head>


<#if !downloads.empty>
	<table class="list">
		<tr>
			<th><@s.text name="label.datecreated" /></th>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.status" /></th>
			<th><@s.text name="label.expires" /></th>
			<th></th>
		</tr>
		<#list downloads as download>
		
			<#if !download.downloaded>
				<#assign style="strong">
			<#else>
				<#assign style="">
			</#if>
			<tr id="dl_${download.id}" class="${style}">
				<td >${action.formatDateTime(download.created)}</td>
				<td id="download_${download.id}">
					${download.name} 
					<span class="normal"> |
						<a href="javascript:void(0);" onClick="editDownloadName(${download.id});"><@s.text name="label.edit" /></a>
					</span>
				</td>
				<td >
					<@s.text name="${download.state.label}"/>
				</td>
				<td >
					&nbsp;
					<#if download.state.willExpire>
						${action.getExpiresText(download.created)}
					</#if>
				</td>
				<td class="normal" > 
					&nbsp;
					<#if download.state.ready>
						<a href="<@s.url action="downloadFile" includeParams="get" fileId="${download.id}" />" onClick="markDownloaded('dl_${download.id}');" ><@s.text name="label.download"/></a>
						|
						<a href='<@s.url action="emailDownload" namespace="/aHtml/iframe" fileId="${download.id}"/>' class='lightview' rel='iframe' title=" :: :: autosize: true, scrolling:true, width: 900, height: 435" ><@s.text name="label.share" /></a>
						|
						<a href="<@s.url action="deleteDownload" fileId="${download.id}" />"><@s.text name="label.delete"/></a>
					</#if>
				</td>
			</tr>	
		</#list>
	</table>
<#else>
	<div class="initialMessage" >
		<div class="textContainer" >
			<h1><@s.text name="label.nodownloads" /></h1>
			<p><@s.text name="message.nodownloads" /></p>
		</div>
	</div>
</#if>

