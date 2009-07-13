<%@ taglib prefix="s" uri="/struts-tags" %>
 
<p> you can only have integration or job sites selected as extended features.</p>
<s:form action="organizationCrud!save" method="post" enctype="multipart/form-data">
	<s:hidden name="id" value="%{id}" /> 
	<s:if test="!id" >
		<s:select name="accountType" label="Account Type" list="%{tenantTypes}" listKey="id" listValue="name" />
	</s:if>
	<s:if test="id">
		<s:select name="accountType" label="Account Type" list="%{tenantTypes}" listKey="id" listValue="name" disabled="true"/>
	</s:if>
	
	<s:label name="tenant.fidAC" label="FidAC" />
	<s:textfield name="tenant.name" value="%{tenant.name}" label="Name" />
	<s:textfield name="tenant.displayName" value="%{tenant.displayName}" label="Display Name" />
	<s:textfield name="tenant.adminEmail" value="%{tenant.adminEmail}" label="Admin Email" />
	<s:checkbox name="tenant.usingSerialNumber" value="%{tenant.usingSerialNumber}" label="Using serial number" />
	<s:textfield name="tenant.serialNumberFormat" value="%{tenant.serialNumberFormat}" label="Serial Number Format" />
	<s:textfield name="tenant.dateFormat" value="%{tenant.dateFormat}" label="Date Format" />
	<s:textfield name="tenant.website" value="%{tenant.webSite}" label="Web Site url" />
	<s:label name="otherDateFormat" label="Other Date Format" />
	<s:label name="formattedDate" label="Example Date" />
	
	<s:if test="!id">
		<s:textfield name="userId" value="%{userId}" label="Admin UserID" />
		<s:password name="userPass" label="Admin Pass" />
		<s:textfield name="userFirstName" value="%{userFirstName}" label="First Name" />
		<s:textfield name="userLastName" value="%{userLastName}" label="Last Name" />
	</s:if>
	
	<s:iterator  value="availableExtendedFeatures" >
		<s:checkbox name="extendedFeatures['%{name}']" >
			<s:param name="label"><s:property value="name"/></s:param>
		</s:checkbox> 
	</s:iterator>
	<s:file name="logoFile" label="Logo file" />
	<s:file name="certificateLogoFile" label="Certificate logo file" />
	<s:submit />
	<s:submit value="Cancel" name="redirect-action:organizationList" />
</s:form>
<pre>
Serial Number Format Options:
%m = Month as a 2 digit number
%d = Day of month as a 2 digit number
%Y = 4 digit year
%y = 2 digit year
%H = Hour of day
%M = Minute of day
%S = Second of day
%L = Millisecond of day
%j = Jergen's style date code
%g = Autoincrementing counter (defaults to 6 digits, reset every year)
</pre>
