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
	$('stateRow_${buttonGroupIndex}').scrollTo();
	$('stateSet_${buttonGroupIndex}_form_name').setStyle({color:'#999'});
	$('stateSet_${buttonGroupIndex}_form_name').observe('click', function(){
		$('stateSet_${buttonGroupIndex}_form_name').value="";
		$('stateSet_${buttonGroupIndex}_form_name').setStyle({color:'black'});
	});
</#escape>