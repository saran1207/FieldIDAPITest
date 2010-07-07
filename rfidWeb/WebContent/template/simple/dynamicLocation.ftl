  <ul class="menu hide" id="${parameters.id}">

	<li class="leaf level"><p class="treeHeading">${(parameters.nodesList?first.levelName?html)!}</p></li>
		<li class="leaf"><a href="#" nodeId="-1" nodeName=""><@s.text name="label.none"/></a></li>
	<#macro createTree entry>

	  <#if !entry.leaf >
	
	  	<li class="expanded"><a href="#" nodeId="${entry.id!}" nodeName="${(entry.name?html)!}">${(entry.name?html)!}</a><br /> 
	      <ul class="menu">
	  	  	<li class="leaf"><p class="treeHeading">${(entry.levelName?html)!}</p></li>
	  	 	<#list entry.children as subentry>
	  			<@createTree entry=subentry/>
	  		</#list>
	 	   </ul> 
	 	</li>
	 <#else>
	 	<li class="leaf"><a href="#" nodeId="${entry.id!}" nodeName="${(entry.name?html)!}">${(entry.name?html)!}</a></li> 
	 </#if>
	</#macro> 

	<#list nodesList as entry>
		<@createTree entry=entry/>
	</#list>
  </ul> 
  
  <script type="text/javascript" charset="utf-8">
    jQuery(document).ready(function(){
      jQuery('#${parameters.id}').columnview({defaultSelection:'${parameters.nameValue!"-1"}'});
    });
  </script>


