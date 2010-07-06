<li class="node" id="location_${node_index}">

	<@s.textfield name="nodes[${node_index}].name" theme="simple"/> 
	
	<a href="javascript:void(0); " onclick=" removeLocation( ${node_index} ); return false;"><@s.text name="label.remove"/></a>

</li>