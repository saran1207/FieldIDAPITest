<head>
	<#include "/templates/html/common/_lightView.ftl"/>
	<script type="text/javascript">
	     function redirectToDownloads(url) {
	        Lightview.hide();
	        window.parent.location=url;
        }
	</script>
</head>
<@s.form action="emailDownloadLink" id="emailDownloadLink" cssClass="crudForm searchForm" theme="fieldid" cssStyle="fullForm fluidSets" >
	<@s.hidden name="fileId" value="${fileId}" />

	<h1><@s.text name="message.email_download_link_heading" ><@s.param>download name</@s.param></@s.text> </h1>
	<p><@s.text name="message.download_link_description" /><@s.text name="message.download_link_warning" /></p>
	
	
	<label><@s.text name="label.emailaddresses" /></label><br/>
	<@s.textfield name="recipients" /><br/><br/>
	<label><@s.text name="label.message" /></label><br/>
	<@s.textarea name="message" /><br/>
	<input type="submit" onclick="return redirectToDownloads('<@s.url namespace="/" action="showDownloads"/>'); return false;" value="<@s.text name="label.submit"/>" />
</@s.form>