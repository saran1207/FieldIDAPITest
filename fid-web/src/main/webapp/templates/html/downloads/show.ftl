${action.setPageType('my_account', 'downloads')!}

<head>
	<#if fileId?exists>
		<meta http-equiv="refresh" content="0;url=<@s.url action="downloadFile" includeParams="get" />"></meta>
	</#if>
	<@n4.includeStyle href="downloads" type="page"/>
	<@n4.includeScript src="downloads.js" />
	<script type="text/javascript">
		editDownloadNameUrl = '<@s.url action="editDownloadName" namespace="/ajax"/>'
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
			<tr>
				<td>${action.formatDateTime(download.created)}</td>
				<td id="download_${download.id}">
					${download.name} |
					<a href="javascript:void(0);" onClick="editDownloadName(${download.id});"><@s.text name="label.edit" /></a>
				</td>
				<td><@s.text name="${download.state.label}"/></td>
				<td>
					&nbsp;
					<#if download.state.willExpire>
						${action.getExpiresText(download.created)}
					</#if>
				</td>
				<td>
					&nbsp;
					<#if download.state.ready>
						<a href="<@s.url action="downloadFile" includeParams="get" fileId="${download.id}" />"><@s.text name="label.download"/></a>
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

