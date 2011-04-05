<head>
	<@n4.includeStyle href="downloads" type="page"/>
	<#include "/templates/html/common/_lightView.ftl"/>
	<script type="text/javascript">
	     function redirectToDownloads(url) {
	        Lightview.hide();
	        window.parent.location=url;
        }
	</script>
</head>

<@s.form action="emailDownloadLink" id="emailDownloadLink" cssClass="searchForm" theme="fieldid" cssStyle="fullForm fluidSets" >
	<@s.hidden name="fileId" value="${fileId}" />

	<h1><@s.text name="message.email_download_link_heading" ><@s.param>${downloadLink.name}</@s.param></@s.text> </h1>
	<p class="description"><@s.text name="message.download_link_description" /></p>
	<br/>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.emailaddresses" /></label><br/>
		<@s.textfield name="recipients" /><br/><br/>
	</div>
	<br/>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.message" /></label><br/>
		<@s.textarea name="message" /><br/>
	</div>
	
	
	<input type="submit" onclick="return redirectToDownloads('<@s.url namespace="/" action="showDownloads"/>'); return false;" value="<@s.text name="label.submit"/>" />
</@s.form>