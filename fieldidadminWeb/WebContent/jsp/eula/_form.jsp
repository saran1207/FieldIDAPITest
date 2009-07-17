<%@ taglib prefix="s" uri="/struts-tags" %>
<s:fielderror/>
<s:hidden name="id" />
<p>
	<label>Effective date</label>
	<span><s:textfield name="effectiveDate" size="100"/></span>
</p>
<p>
	<label>Version</label>
	<span><s:textfield name="version" size="100"/></span>
</p>
<p>
	<label>Legal Text</label>
	<span><s:textarea name="legalText" rows="100" cols="200"/></span>
</p>
<s:reset name="cancel" value="cancel" onclick="window.location='eulas.action'; return false;"/> <s:submit value="save" name="save"/>