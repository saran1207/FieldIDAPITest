<@s.actionerror />
<@s.actionmessage />

<h2>Export All Customers and Divisions</h2>
<@s.form action="exportAll" method="post">
	<div>
		<@s.select name="tenantId" list="tenants" />
		<@s.submit value="Export" />
	</div>
</@s.form>

<h2>Import Customers and Divisions</h2>
<@s.form action="importAll" method="post" enctype="multipart/form-data">
	<div>
		<@s.select name="tenantId" list="tenants" />
		<@s.file name="upload"/>
		<@s.submit value="Import" />
	</div>
</@s.form>