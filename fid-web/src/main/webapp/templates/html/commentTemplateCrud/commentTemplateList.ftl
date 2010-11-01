${action.setPageType('comment_template', 'list')!}

<head>
	<@n4.includeStyle href="commentList" type="page"/>
</head>

<#if !commentTemplates.isEmpty() >
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
<#else>
	<@s.url id="addCommentTemplateUrl" action="commentTemplateEdit"/>
	
	<div class="initialMessage">
		<div class="textContainer" >
			<h1><@s.text name="label.create_comment_template"/></h1>
			<p><@s.text name="label.create_comment_template_message" /></p>
		</div>
			<input type="submit" value="<@s.text name="label.create_comment_template_now"/>"onclick="return redirect('${addCommentTemplateUrl}');"/>
	</div>
</#if>
</table>
