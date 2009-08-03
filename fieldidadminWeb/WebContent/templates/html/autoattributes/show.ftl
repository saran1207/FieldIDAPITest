<h2>Import Auto-Attributes</h2>

<@s.form action="importAutoAttributes!import" method="POST" enctype="multipart/form-data">
	
	 <@s.doubleselect
		name="tenantId"				doubleName="productTypeId"
		list="tenantView"			doubleList="subList"
		listKey="id"				doubleListKey="id"
		listValue="displayName"		doubleListValue="displayName"
		label="Tenant/ProductType"	labelposition="left" />
	
	<@s.file name="autoAttributesCsv" accept="text/*" size="32" label="Auto-Attributes CSV" />
	
	<@s.submit value="Send" />
</@s.form>

<br />
<@s.actionerror />
<@s.actionmessage />