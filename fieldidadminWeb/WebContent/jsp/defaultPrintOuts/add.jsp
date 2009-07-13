<%@ taglib prefix="s" uri="/struts-tags" %>
<h1>Add Default Print Out</h1>
<style>
	label {
		display:block;
	}
</style>
<s:form action="defaultPrintOutCreate" theme="simple" >
    <jsp:include page="_form.jsp" />
</s:form>