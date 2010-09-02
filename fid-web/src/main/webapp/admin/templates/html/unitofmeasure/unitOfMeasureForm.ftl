
<@s.form action="unitOfMeasureCrud!save" namespace="/admin" method="post" theme="xhtml">
	<@s.hidden name="id" value="%{id}" />
	<@s.textfield name="unitOfMeasure.name" value="%{unitOfMeasure.name}" label="Name" />
	<@s.textfield name="unitOfMeasure.type" value="%{unitOfMeasure.type}" label="Type" />
	<@s.textfield name="unitOfMeasure.shortName" value="%{unitOfMeasure.shortName}" label="Short name" />
	<@s.checkbox name="unitOfMeasure.selectable" value="%{unitOfMeasure.selectable}" label="Selectable" />
	<@s.select name="unitOfMeasure.child.id" value="%{unitOfMeasure.child.id}" list="unitOfMeasures" listValue="name" emptyOption="true" listKey="id" label="Child" />
	<@s.submit />
</@s.form>
	