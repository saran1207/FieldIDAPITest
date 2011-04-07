<head>
	<@n4.includeStyle href="downloads" type="page"/>
	<#include "/templates/html/common/_lightView.ftl"/>

</head>

<@s.form action="emailDownloadLink" id="emailDownloadLink" cssClass="searchForm" theme="fieldid" cssStyle="fullForm fluidSets" >
	<@s.hidden name="fileId" value="${fileId}" />
	<h1><@s.text name="message.email_download_link_heading" ><@s.param>"${downloadLink.name}"</@s.param></@s.text> </h1>
	<p class="description"><@s.text name="message.download_link_description" /></p>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.emailaddresses" /></label><br/>
		<@s.textfield id="recipients" name="recipients" /><br/><br/>
	</div>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.message" /></label><br/>
		<@s.textarea name="message" /><br/>
	</div>
	
	
	<input type="submit" value="<@s.text name="label.submit"/>" />
</@s.form>

<script type="text/javascript">
	Event.observe('emailDownloadLink','submit', function(event){
		if ($('recipients').value==''){
    		alert('Please enter one or more email addresses');
    		Event.stop(event);
    	}else{
    		Lightview.hide();
        	window.parent.location.reload(true);
    	}
	})

</script>