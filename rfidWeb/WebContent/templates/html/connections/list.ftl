${action.setPageType('safety_network_connections', 'list')!}



<h2><@s.text name="label.myvendorconnections"/></h2>

<table class="list">
	<tr>
		<th colspan="2"><@s.text name="label.myorganizations" /></th>
		<th colspan="2"><@s.text name="label.vendororganizations" /></th>
	</tr>
	<tr>
		<th><@s.text name="label.primaryorganization" /></th>
		<th><@s.text name="label.organizationalunit" /></th>		
		<th><@s.text name="label.primaryorganization" /></th>
		<th><@s.text name="label.organizationalunit" /></th>
	</tr>
	
	<#list vendorConnections as vendConn>
	<tr>
		<td>${(vendConn.customer.primaryOrg.name)!}</td>
		<td>${(vendConn.customer.secondaryOrg.name)!}</td>
		<td>${(vendConn.vendor.primaryOrg.name)!}</td>
		<td>${(vendConn.vendor.secondaryOrg.name)!}</td>
	</tr>
	</#list>
</table>
<hr>

<h2><@s.text name="label.mycustomerconnections"/></h2>

<table class="list">
	<tr>
		<th colspan="2"><@s.text name="label.myorganizations" /></th>
		<th colspan="2"><@s.text name="label.customerorganizations" /></th>
	</tr>
	<tr>
		<th><@s.text name="label.primaryorganization" /></th>
		<th><@s.text name="label.organizationalunit" /></th>		
		<th><@s.text name="label.primaryorganization" /></th>
		<th><@s.text name="label.organizationalunit" /></th>
	</tr>
	
	<#list customerConnections as custConn>
	<tr>
		<td>${(custConn.vendor.primaryOrg.name)!}</td>
		<td>${(custConn.vendor.secondaryOrg.name)!}</td>
		<td>${(custConn.customer.primaryOrg.name)!}</td>
		<td>${(custConn.customer.secondaryOrg.name)!}</td>
	</tr>
	</#list>
</table>


<h2><@s.text name="label.connections"/></h2>

<ul id="safetyNetwork">
	<#list connections.list as connection>
		<li>
			${connection.name?html}
			<#if action.hasAPublishedCatalog(connection)>
				<a href="<@s.url action="publishedCatalog" uniqueID="${connection.tenant.id}"/>"><@s.text name="view_catalog"/></a>
			</#if>
		</li>
	</#list>
</ul>