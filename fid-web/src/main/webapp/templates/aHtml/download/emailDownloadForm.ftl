<head>
	<@n4.includeStyle href="downloads" type="page"/>
	<#include "/templates/html/common/_lightView.ftl"/>

</head>

<@s.form action="emailDownloadLink" id="emailDownloadLink" cssClass="searchForm" theme="fieldid" cssStyle="fullForm fluidSets" >
	<@s.hidden name="fileId" value="${fileId}" />
	<h1><@s.text name="message.email_download_link_heading" ><@s.param>"${downloadLink.name}"</@s.param></@s.text> </h1>
	<p class="description"><@s.text name="message.download_link_description" /></p>
	
	<div class="inputContainer">
		<div class="infoSet">
			<label class="label"><@s.text name="label.emailaddresses" /></label><br/>
			<@s.textfield id="recipients" name="recipients" /><br/><br/>
			<p class="lightColored positioned"><@s.text name="label.separate_emails_by_commas" /></p>	
		</div>
		
		<div class="infoSet">
			<label class="label"><@s.text name="label.message" /></label><br/>
			<@s.textarea name="message" /><br/>
		</div>
		
		<input type="submit" value="<@s.text name="label.send_email_now"/>" />&nbsp;<@s.text name="label.or"/>&nbsp;<a href="#" id="cancelLink" ><@s.text name="label.cancel"/></a>
		
	</div>
	
	<div class="emailInstructions">
		<h3><@s.text name="label.correct_formats" /><h3>
		<p class="correctExample"><@s.text name="label.correct_example1" /></p>
		<p class="correctExample"><@s.text name="label.correct_example2" /></p>
		<br/>
		<h3><@s.text name="label.incorrect_formats" /><h3>
		<p class="incorrectExample"><@s.text name="label.incorrect_example1" /><span class="lightColored"><@s.text name="label.incorrect_example_description1" /></span></p>
		<p class="incorrectExample"><@s.text name="label.incorrect_example2" /><span class="lightColored"><@s.text name="label.incorrect_example_description2" /></span></p>
		<p class="incorrectExample"><@s.text name="label.incorrect_example3" /><span class="lightColored"><@s.text name="label.incorrect_example_description3" /></span></p>
	</div>

</@s.form>

<script type="text/javascript">
	$('cancelLink').observe('click', function(){
			Lightview.hide();
			window.parent.location.reload(true);
	});
	
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
