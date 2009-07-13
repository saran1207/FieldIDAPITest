<title>
	<@s.text name="title.report" />
</title>
<@s.form action="savedReportUpdate" theme="fieldid" cssClass="crudForm bigForm pageSection">
	<@s.hidden name="uniqueID"/>
	<#include "_form.ftl"/>
	<div class="formAction">
		<@s.url action="reportResults" searchId="${searchId}" id="cancelUrl"/>
		<@s.reset key="label.cancel" onclick="return redirect('${cancelUrl}');"/> 
		<@s.submit key="label.save"/>
	</div>
</@s.form>