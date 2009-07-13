<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="login" method="post">
	<s:textfield name="username" label="Username" />
	<s:password name="password" label="Password" />
	<s:submit />
</s:form>