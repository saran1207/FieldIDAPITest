
<head>
	<script src="http://code.jquery.com/jquery-1.4.2.min.js" type="text/javascript" charset="utf-8"></script>
    <script>
 	 	jQuery.noConflict(); 
    </script>
  	<script type="text/javascript" src="<@s.url value="/javascript/jquery.columnview.js"/>"></script>
</head>
  
  <ul class="menu" id="original">

	<li class="leaf level"><p class="treeHeading">${nodes?first.levelName}</p></li>
	<#macro recursion entry>

	  <#if !entry.leaf >
	
	  <li class="expanded"><a href="#">${entry.name}</a><br /> 
	      <ul class="menu">
	  	  	<li id="hurf" class="leaf"><p class="treeHeading">${entry.levelName}</p></li>
	  	 	<#list entry.children as subentry>
	  			<@recursion entry=subentry/>
	  		</#list>
	  		<li class="leaf"><input type="text" name="dynamic"/></li>
	 	   </ul> 
	 </li>
		 	
		 	
		  <#else>
		  	<li class="leaf"><a href="#" title="hello roumen">${entry.name}</a></li> 
		  </#if>
	</#macro> 

	<#list nodes as entry>
		<@recursion entry=entry/>
	</#list>
    
  </ul> 
  
<input type="button" name="getactive" value="Hightlight Selected Items" id="getactive"/>
 <input type="button" name="close" value="Close" id="close"/>
  <script type="text/javascript" charset="utf-8">
    jQuery(document).ready(function(){
      jQuery('#original').columnview({multi:true});
      jQuery('#getactive').click(function(){
        jQuery('#original a.active').css({'background-color':'orange'});
       	
         //Pull out the selected element's id.
         //console.log(jQuery('.active').html());
      });
      
       jQuery('#close').click(function(){
        jQuery(this).parent().hide();
         
      });
       
    });
  </script>


