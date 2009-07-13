<%@ taglib prefix="s" uri="/struts-tags" %>
<jsp:include page="../common/_formErrors.jsp" />
<s:hidden name="uniqueID" />
<p>
	<label>Type</label>
	<span><s:select name="printOutType" list="printOutTypes" listKey="name" listValue="name" cssStyle="width:300px"/></span>
</p>
<p>
	<label>Name (100 characters)</label>
	<span><s:textfield name="printOut.name" cssStyle="width:300px"/></span>
</p>
<p>
	<label>Description ( new lines will be turned into br tags when the user sees them )</label>
	<span><s:textarea name="printOut.description" cols="70" rows="10"/></span>
</p>
<p>
	<label>Prints with Sub Inspections</label>
	<span><s:checkbox name="printOut.withSubInspections" /></span>
</p>
<p>
	<label>Jasper template name ( with out .japser on it )</label>
	<span><s:textfield name="printOut.pdfTemplate" cssStyle="width:300px"/></span>
</p>