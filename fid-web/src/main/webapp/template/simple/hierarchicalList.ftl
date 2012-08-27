<#macro createTree entry>
	  <#if !entry.leaf >
          <#if entry.owner?exists>
          <li class="expanded"><a class="node has-owner" href="#" nodeId="${entry.id!}" nodeDisplayName="${(entry.name?html)!}"><span class="name" >${(entry.name?html)!}</span> <span class="org">( ${(entry.owner.displayName?html)!} )</span></a><br/>
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
         <#if entry.owner?exists>
            <li class="leaf"><a class="node has-owner"  href="#" nodeId="${entry.id!}" nodeDisplayName="${(entry.name?html)!}"><span class="name" >${(entry.name?html)!}</span><span class="org"> ( ${(entry.owner.displayName?html)!} )</span></a></li>
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

    #nodeList .org {
        color:#666;
        font-weight: normal;
        font-size:11px;
    }

    #nodeList .active.org {
        color:#444;
    }

    #nodeList .node {
        padding-left:5px;
        padding-bottom:2px;
        font-size:13px;
        color:#44a;
        height:34px;
    }

    #nodeList .active.node {
        color:#44a;
    }

    #nodeList .node .name {
        font-weight:normal;
        display:block;
    }

    #nodeList .node.no-owner .name{
        padding-top:8px;
    }

    .containerobj .active {
        background-color: #C8CFE3;
    }

    .containerobj {
        height:500px;
    }
    .containerobj div {
        height:100%;
    }
</style>
  
  <ul class="menu hide" id="${parameters.id}">

	<li class="leaf level"><p class="treeHeading">${(parameters.nodesList?first.levelName?html)!}</p></li>
	<li class="leaf"><a  class="node none" href="#" nodeId="-1" nodeDisplayName=""><@s.text name="label.none"/></a></li>
	<#list nodesList as entry>
		<@createTree entry=entry/>
	</#list>
  </ul> 
  
  <@n4.includeScript>
    jQuery('#${parameters.id}').columnview({defaultSelection:'${parameters.nameValue!"-1"}'});
  </@n4.includeScript>


