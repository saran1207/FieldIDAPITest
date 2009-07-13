<%@ taglib prefix="s" uri="/struts-tags" %>
<jsp:include page="_commonForm.jsp" />
<s:reset name="cancel" value="cancel" onclick="window.location='defaultPrintOuts.action'; return false;"/> 
<s:submit value="save" name="save"/>