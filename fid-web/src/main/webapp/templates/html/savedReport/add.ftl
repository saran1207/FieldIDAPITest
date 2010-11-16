<title>
	<@s.text name="title.report" />
</title>
<@s.form action="savedReportCreate" theme="fieldid" cssClass="crudForm bigForm pageSection">
	<#include "_form.ftl"/>
	<div class="formAction">
		<@s.url action="reportResults" searchId="${searchId}" id="cancelUrl"/>
		<@s.submit key="label.save"/>
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect('${cancelUrl}');" ><@s.text name="label.cancel"/></a> 
	</div>
</@s.form>