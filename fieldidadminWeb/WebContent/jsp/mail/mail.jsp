<%@ taglib prefix="s" uri="/struts-tags" %>

<h2>Mail Test</h2>

<s:form action="mailTest!send" method="post">
	<s:textfield name="toAddress" value="%{toAddress}" label="To Address" size="40" />
	<s:textfield name="subject" value="%{subject}" label="Subject" size="60" />
	<s:textarea name="body" value="%{body}" label="Body" cols="80" rows="20"/>
	<s:textfield name="attachmentPath" value="%{attachmentPath}" label="File Path" size="30" />
	<s:checkbox name="html" value="html" label="HTML" />
	<s:submit value="Send" />
</s:form>
<br />
<s:actionerror />
<s:actionmessage />