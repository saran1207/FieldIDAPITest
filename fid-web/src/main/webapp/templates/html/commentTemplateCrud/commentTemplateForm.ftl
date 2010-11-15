<head>
	<style>
		.formAction div{
			 padding: 0;
		}	
	</style>
	<title>
		<#if uniqueID?exists  >
			${action.setPageType('comment_template', 'edit')!}
		<#else>
			${action.setPageType('comment_template', 'add')!}
		</#if>
	</title>
</head>
<@s.form action="commentTemplateEdit!save" cssClass="crudForm inputForm oneColumn" theme="css_xhtml">
	<@s.hidden name="uniqueID" />
	<@s.url id="cancelUrl" action="commentTemplateList"/>
		
	<div class="formRowHolder">
		<@s.textfield key="label.name" name="name" size="30" labelposition="left"/>
	</div>
	<div class="formRowHolder">
		<@s.textarea key="label.comment" name="comment" rows="5" cols="50" labelposition="left"/>
	</div>
	<div class="formAction">
		<@s.submit key="hbutton.save" />
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect( '${cancelUrl}' );" />	<@s.text name="label.cancel"/></a>
	
	</div>
</@s.form>  