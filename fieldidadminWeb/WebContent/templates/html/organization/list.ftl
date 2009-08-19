<head>
	<script type="text/javascript" >
		function createSuper( id ) {
			var div = $( "superUser_" + id );
			div.style.display = "block";
			return false;
		}
		function submitSuperUser( button ) {
			button.form.request( { 
				onComplete: 
					function( response ) {
						try { 
							var forms = document.getElementsByClassName( "superUserForm" );
							for( var i = 0; i < forms.length; i++ ) {
								forms[i].style.display = "none";
								
							}
						} catch( e ) {
							alert( e );
						}
					}
			} );
		}
		
	
	</script>
</head>
<table>
Total Tenants: ${primaryOrgs?size}
<#list primaryOrgs as primaryOrg>
<tr>
	<td>${primaryOrg.displayName?html}</td>
	<td>${primaryOrg.tenant.name?html}</td>
	<td><a href="organizationCrud.action?id=${primaryOrg.tenant.id}">Edit</a></td>
	<td>
		<a href="#" onclick="createSuper('${primaryOrg.tenant.id}'); return false;">Create Super User</a> 
		<div id="superUser_${primaryOrg.tenant.id}" class="superUserForm" style="display:none">
			<@s.form action="createSuperUser" namespace="/ajax" method="post">
				<input type="hidden" name="id" value="${primaryOrg.tenant.id}" />
				<@s.textfield name="userName" label="Default User Name" />
				<@s.password name="password" label="Default User Password" />
				<@s.submit name="save" onclick="submitSuperUser(this); return false;" />
			</@s.form>
		</div>
	</td>
</tr>
</#list>
</table>

<a href="<@s.url action="organizationCrud" />">Add New Tenant</a><br />