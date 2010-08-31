
<h2>Screens</h2>
<table>
<tr><th>Description</th><th>&nbsp;</th></tr>
<@s.iterator id="screen" value="screens">
	<tr><td><@s.property value="description" /></td><td><a href="screenCrud.action?id=<@s.property value="uniqueID" />">Edit</a></tr>
</@s.iterator>
</table>
<a href="screenCrud.action">Add New Screen</a>