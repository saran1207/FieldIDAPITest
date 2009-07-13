<%@ taglib prefix="s" uri="/struts-tags" %>
<h1>Edit default Print out</h1>
<style>
	label {
		display:block;
	}
</style>
<s:form action="defaultPrintOutUpdate" theme="simple" >
    <jsp:include page="_form.jsp" />
</s:form>