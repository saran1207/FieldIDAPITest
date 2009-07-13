<%@ taglib prefix="s" uri="/struts-tags" %>
<h1>Add Custom Print Out</h1>
<style>
	label {
		display:block;
	}
</style>
<s:form action="customPrintOutCreate" theme="simple" >
    <jsp:include page="_form.jsp" />
</s:form>