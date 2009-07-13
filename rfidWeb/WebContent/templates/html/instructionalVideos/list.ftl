<title><@s.text name="title.instrunctionalvideos"/></title>



<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="instructionalVideos.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.title" /></th>
			<th><@s.text name="label.dateadded" /></th>
		</tr>
		<#list page.getList() as video > 
			<tr id="video_${video.id}" >
				<td>${video.name?html} - <a href="${video.url}" target="_blank"><@s.text name="label.watchnow"/></a></td>
				<td>${action.formatDate(video.created, true)}</td>
				
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyinstructionalvideolist" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="instructionalVideos" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
	


