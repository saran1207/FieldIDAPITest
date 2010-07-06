  <ul class="menu hide" id="${parameters.id}">

	<li class="leaf level"><p class="treeHeading">${(parameters.nodesList?first.levelName?html)!}</p></li>
	<#macro recursion entry>

	  <#if !entry.leaf >
	
	  	<li class="expanded"><a href="#" nodeId="${entry.id}">${(entry.name?html)!}</a><br /> 
	      <ul class="menu">
	  	  	<li class="leaf"><p class="treeHeading">${(entry.levelName?html)!}</p></li>
	  	 	<#list entry.children as subentry>
	  			<@recursion entry=subentry/>
	  		</#list>
	 	   </ul> 
	 	</li>
	 <#else>
	 	<li class="leaf"><a href="#" nodeId="${entry.id}">${(entry.name?html)!}</a></li> 
	 </#if>
	</#macro> 

	<#list nodesList as entry>
		<@recursion entry=entry/>
	</#list>
  </ul> 
  
<input type="button" name="getactive" value="<@s.text name="label.select_location"/>" id="${parameters.id}_getactive"/>
 <input type="button" name="close" value="<@s.text name="label.close"/>" id="${parameters.id}_close"/>
  <script type="text/javascript" charset="utf-8">
    jQuery(document).ready(function(){
      jQuery('#${parameters.id}').columnview({defaultSelection:'${parameters.nameValue!}'});
    });
  </script>


