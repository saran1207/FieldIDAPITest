<title>
	<#if uniqueID?exists  >
		${action.setPageType('comment_template', 'edit')!}
	<#else>
		${action.setPageType('comment_template', 'add')!}
	</#if>
</title>

<@s.form action="commentTemplateEdit!save" cssClass="inputForm oneColumn" theme="css_xhtml">
	<@s.hidden name="uniqueID" />
	<div class="formRowHolder">
		<@s.textfield key="label.name" name="name" size="30" labelposition="left"/>
	</div>
	<div class="formRowHolder">
		<@s.textarea key="label.comment" name="comment" rows="5" cols="50" labelposition="left"/>
	</div>
	<div class="formAction">
		<@s.submit key="hbutton.cancel" name="redirect-action:commentTemplateList" /><@s.submit key="hbutton.save" />
	</div>
</@s.form>  