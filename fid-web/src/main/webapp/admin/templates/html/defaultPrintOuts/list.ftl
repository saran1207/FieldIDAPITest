<h1>Default Print Outs</h1>

<a href='<@s.url action="defaultPrintOutAdd" namespace="/admin" />'>add new default print out</a>

<#if page.hasResults() && page.validPage() >
	
	
	<#include "/templates/html/common/_pagination.ftl" />
	<table class="list">
		<tr>
			<th>name</th>
			<th>description</th>
			<th>type</th>
			<th></th>
			<th></th>
		</tr>
		<@s.iterator id="printOut" value="page.getList()">
			<tr id="printOut_<@s.property value="id"/>" >
				<td><a href="<@s.property value="url"/>"><@s.property value="name"/></a></td>
				<td><@s.property value="description"/></td>
				<td><@s.property value="type.name()"/></td>
				<td><a href="defaultPrintOutEdit.action?uniqueID=<@s.property value="id"/>">edit</a></td>
				<td><a href="defaultPrintOutDelete.action?uniqueID=<@s.property value="id"/>">delete</a></td>
			</tr>	
		</@s.iterator>
	</table>
<#else >
	<div class="emptyList" >
		<h2>no results</h2>
		<p>
			there are no default print outs
		</p>
	</div>
</#if>

	


