
<h1>Instrunctional Videos</h1>

<a href='<@s.url action="instructionalVideoAdd" namespace="/admin" includeParams="none"/>'>add video</a>

<@s.if  test="%{page.hasResults() && page.validPage()}" >
	
	
	<#include "/admin/templates/html/common/_pagination.ftl" />
	<table class="list">
		<tr>
			<th>title</th>
			<th>date added</th>
			<th></th>
		</tr>
		<@s.iterator id="video" value="page.getList()">
			<tr id="video_<@s.property value="id"/>" >
				<td><a href="<@s.property value="url"/>"><@s.property value="name"/></a></td>
				<td><@s.property value="created"/></td>
				<td><a href="instructionalVideoEdit.action?id=<@s.property value="id"/>">edit</a></td>
				<td><a href="instructionalVideoDelete.action?id=<@s.property value="id"/>">delete</a></td>
			</tr>	
		</@s.iterator>
	</table>
</@s.if>	
<@s.else >
	<div class="emptyList" >
		<h2>no results</h2>
		<p>
			there are no instructional videos.
		</p>
	</div>
</@s.else>

	


