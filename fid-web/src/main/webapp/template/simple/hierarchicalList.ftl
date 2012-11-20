<#macro createTree entry>
	  <#if !entry.leaf >

          <#if entry.levelNumber==1>
            <li class="expanded"><a class="node has-owner" href="#" nodeId="${entry.id!}" nodeDisplayName="${(entry.name?html)!}"><span class="name" >${(entry.name?html)!}</span> <span class="org"> ${(entry.owner.displayName?html)!}</span></a><br/>
          <#else>
            <li class="expanded"><a class="node no-owner" href="#" nodeId="${entry.id!}" nodeDisplayName="${(entry.name?html)!}"><span class="name">${(entry.name?html)!}</span> </a><br/>
          </#if>
          <ul class="menu">
	  	  	<li class="leaf"><p class="treeHeading">${(entry.children?first.levelName?html)!}</p></li>
	  	 	<#list entry.children as subentry>
	  			<@createTree entry=subentry/>
	  		</#list>
	 	   </ul> 
	 	</li>
	 <#else>
         <#if entry.levelNumber==1>
            <li class="leaf"><a class="node has-owner"  href="#" nodeId="${entry.id!}" nodeDisplayName="${(entry.name?html)!}"><span class="name" >${(entry.name?html)!}</span><span class="org">  ${(entry.owner.displayName?html)!} </span></a></li>
         <#else>
             <li class="leaf"><a class="node no-owner"  href="#" nodeId="${entry.id!}" nodeDisplayName="${(entry.name?html)!}"><span class="name" >${(entry.name?html)!}</span> </a></li>
         </#if>
	 </#if>
</#macro>

<style>
    .node.has-owner .widget{
        margin-top:-15px;
        color:#888;
    }

    .node.no-owner .widget{
        margin-top:-23px;
        color:#888;
    }

    .containerobj .node .org, .containerobj .node .name {
        padding-top:0px;
        padding-bottom:0px;
    }

    .containerobj .org {
        color:#666;
        font-weight: normal;
        font-size:11px;
    }

    .containerobj .active.org {
        color:#444;
    }

    .containerobj .node {
        padding-left:5px;
        padding-bottom:2px;
        font-size:13px;
        color:#669;
        height:34px;
    }

    .containerobj .active.node {
        color:#44a;
    }

    .containerobj .node .name {
        font-weight:normal;
        display:block;
    }

    .containerobj .node.no-owner .name{
        padding-top:8px;
    }

    .containerobj .active {
        background-color: #C8CFE3;
    }

    .containerobj div {
        height:100%;
    }

    /*make it bigger on PredefinedLocations edit screen.*/
    #nodeList {
        height:600px;
    }
</style>
  
  <ul class="menu hide" id="${parameters.id}">

	<li class="leaf level"><p class="treeHeading">${(parameters.nodesList?first.levelName?html)!}</p></li>
	<#list nodesList as entry>
		<@createTree entry=entry/>
	</#list>
  </ul> 
  
  <@n4.includeScript>
    jQuery('#${parameters.id}').columnview({defaultSelection:'${parameters.nameValue!"-1"}'});
  </@n4.includeScript>


