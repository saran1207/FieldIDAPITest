


<h2>All Data List</h2>

<@s.form action="importAllData.action" theme="simple">
	Tenant <@s.select id="manufacturer" list="tenants" listValue="displayName" listKey="name" label="All Tenants" name="tenantName" />
	<@s.submit value="Show List"></@s.submit>
</@s.form>


<div class="fileList">
	<p>Importing Files</p>
<@s.iterator id="file" value="importingFiles">
	<div>
		<@s.property id="file" value="name" />		
	</div>
</@s.iterator>
</div>
<div class="fileList">
	<p>Processing Files</p>
	<@s.iterator id="file" value="processingFiles">
	<div>
		<@s.property id="file" value="name" />		
	</div>
</@s.iterator>
</div>
<div class="clearBoth"></div>
<@s.form action="importAllData!importAll" method="post">
	<@s.hidden id="tenantName" name="tenantName" />
	<@s.checkbox label="create missing divisions" id="createMissingDivisions" name="createMissingDivisions" />
	<@s.submit value="Import All Files"></@s.submit>
</@s.form>

<a href="importerList.action">Back</a> 
