<#escape x as x?j_string >
<#assign index=buttonGroupIndex/>
<#assign html>
	<tr id="stateRow_${buttonGroupIndex}">
		<td colspan="2"> 
			<#include "/templates/html/buttonGroupCrud/_form.ftl" >
		</td>
	</tr>
</#assign>
	
	$('buttonGroups').insert( "${html}" );
	enableGroupSaveButton( ${buttonGroupIndex} );

</#escape>