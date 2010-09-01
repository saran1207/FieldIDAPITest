

<h2>Import Observations</h2>

<@s.form action="importObservations!import" method="POST" namespace="/admin" enctype="multipart/form-data">

	<@s.select name="tenantId" list="tenants" size="1" label="Tenant" />
	<@s.file name="observationCsv" accept="text/*" size="32" label="Observation CSV" />
	
	<@s.submit value="Send" />
</@s.form>

<br />
<@s.actionerror />
<@s.actionmessage />