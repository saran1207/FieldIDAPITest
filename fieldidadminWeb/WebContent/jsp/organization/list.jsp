<%@ taglib prefix="s" uri="/struts-tags" %>

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
<table>
<s:iterator id="manufacture" value="organizations">
<tr>
	<td><s:property value="displayName" /></td>
	<td><s:property value="name" /></td>
	<td><a href="organizationCrud.action?id=<s:property value="id" />">Edit</a></td>
	<td>
		<a href="#" onclick=" createSuper( '<s:property value="id"/>' ); return false;">Create Super User</a> 
		<div id="superUser_<s:property value="id"/>" class="superUserForm" style="display:none">
			<s:form action="createSuperUser" namespace="/ajax" method="post">
				<input type="hidden" name="id" value="<s:property value="id" />" />
				<s:textfield name="userName" label="Default User Name" />
				<s:password name="password" label="Default User Password" />
				<s:submit name="save" onclick="submitSuperUser( this ); return false;" />
			</s:form>
		</div>
	</td>
</tr>
</s:iterator>
</table>
<s:url id="addUrl" action="organizationCrud" />
<a href="<s:property value="#addUrl" />">Add New Tenant</a><br />
Size: <s:property value="size" />



