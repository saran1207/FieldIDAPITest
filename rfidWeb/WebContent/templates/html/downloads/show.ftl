${action.setPageType('my_account', 'downloads')!}

<head>
	<#if fileId?exists>
		<meta http-equiv="refresh" content="0;url=<@s.url action="downloadFile" includeParams="get" />"></meta>
	</#if>
</head>

<h2 class="sectionTitle"><@s.text name="label.downloads_exports"/></h2>

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
				<td>${download.name}</td>
				<td><@s.text name="${download.state.label}"/></td>
				<td>
					<#if download.state.ready>
						${action.getExpiresText(download.created)}
					</#if>
				</td>
				<td>
					<#if download.state.ready>
						<a href="<@s.url action="downloadFile" includeParams="get" fileId="${download.id}" />"><@s.text name="label.download"/></a>
					</#if>
				</td>
			</tr>	
		</#list>
	</table>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.nodownloads" /></h2>
	</div>
</#if>

<h2 class="sectionTitle"><@s.text name="label.downloads_software"/></h2>
