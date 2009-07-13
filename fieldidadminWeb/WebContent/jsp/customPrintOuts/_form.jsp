<%@ taglib prefix="s" uri="/struts-tags" %>
<jsp:include page="../defaultPrintOuts/_commonForm.jsp" />
<p>
	<label>Tenant * </label>
	<span><s:select name="tenant" list="tenants" listKey="id" listValue="name" cssStyle="width:300px"/></span>
</p>

<s:reset name="cancel" value="cancel" onclick="window.location='customPrintOuts.action'; return false;"/> 
<s:submit value="save" name="save"/>