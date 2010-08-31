
<@s.form action="orderMappingCrud!save" method="post">
	<@s.hidden name="id" value="%{id}" />
	<@s.textfield name="orderMapping.organizationID" value="%{orderMapping.organizationID}" label="Organization ID" />
	<@s.textfield name="orderMapping.externalSourceID" value="%{orderMapping.externalSourceID}" label="External Source ID" />
	<@s.select list="keys" value="%{orderKey}" name="orderKey" label="Our key" />
	<@s.textfield name="orderMapping.sourceOrderKey" value="%{orderMapping.sourceOrderKey}" label="Their key" />
	<@s.submit />
</@s.form>