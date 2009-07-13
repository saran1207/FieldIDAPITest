<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="errors">
	<s:if test="%{!fieldErrors.isEmpty()}">
		<div class="formErrors" >
			<h2>Errors:</h2>
			<s:fielderror />
		</div>
	</s:if>
	
</div>