<%@ taglib prefix="s" uri="/struts-tags" %>
<h2>System Password Change</h2>

<s:actionerror />
<s:actionmessage />
<s:form action="changeAdminPass!save" method="post">
<div>
	<s:password name="pass1" label="Password" />
	<s:password name="pass2" label="Again" />
	<s:submit value="Save" />
</div>
</s:form>