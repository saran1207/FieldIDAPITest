${action.setPageType('comment_template', 'list')!}
<table class="list">
<tr>
	<th><@s.text name="label.commenttemplate" /></th>
	<th></th>
<tr>

<#list commentTemplates as commentTemplate>
	<tr>
		<td>${commentTemplate.displayName?html}</td>
		<td>
			<a href="<@s.url value="commentTemplateEdit.action" uniqueID="${commentTemplate.id}" />" ><@s.text name="label.edit" /></a>&nbsp;&nbsp;
			<a href="<@s.url value="commentTemplateRemove.action" uniqueID="${commentTemplate.id}" />" onclick="return confirm('<@s.text name="label.areyousure" />');" ><@s.text name="label.remove" /></a>
		</td>
	</tr>
</#list>
</table>
