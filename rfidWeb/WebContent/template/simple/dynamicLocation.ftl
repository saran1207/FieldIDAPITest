<head>
	<script src="http://code.jquery.com/jquery-1.4.2.min.js" type="text/javascript" charset="utf-8"></script>
    <script>
  		jQuery.noConflict();
    </script>
    <script type="text/javascript" src="<@s.url value="/javascript/jquery.columnview.js"/>"></script>
</head>
    
 	<ul class="menu" id="original">

	<li class="leaf level"><p class="heading">Base Level</p></li>
	<#macro recursion entry>
  			
		  <#if !entry.leaf >
		
		  <li class="expanded"><a href="#">${entry.name}</a><br /> 
		 
		  	<ul class="menu">
					<ul class="menu" id="original">
				  		<ul class="menu">
				  		 	<li id="hurf" class="leaf heading"><p>${entry.levelName}</p></li>
				  		</ul>
				  </ul> 
		  	 
		  	 	<#list entry.children as subentry>
		  			<@recursion entry=subentry/>
		  		</#list>
		  		<li class="leaf"><input type="text" name="dynamic"/></li>
		  		
		 	</ul> 
		 	
		  <#else>
		  	<li class="leaf"><a href="#" title="hello roumen">${entry.name}</a></li> 
		  </#if>
	</#macro> 

	<#list parameters.nodesList as entry>
		<@recursion entry=entry/>
	</#list>
    
  </ul> 
  
<input type="button" name="getactive" value="Hightlight Selected Items" id="getactive"/>
  
  <script type="text/javascript" charset="utf-8">
    jQuery(document).ready(function(){
      jQuery('#original').columnview({multi:true});
      jQuery('#getactive').click(function(){
        jQuery('#original a.active').css({'background-color':'orange'});
       	
         console.log(jQuery('.active').html());
      });
    });
  </script>