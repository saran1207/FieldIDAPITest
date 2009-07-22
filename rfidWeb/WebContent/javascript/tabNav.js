	function toggleDisp(d) {
	    
	    
	    if (d.style.display == 'none') {
	            d.style.display = 'block';
	        } else {
	            d.style.display = 'none';
	        }
	    
	}	

	function toggleTab( tabContainerName, tabName ) {
		var containerPostfix  = "_container";
	    var tab = $(tabName);
	    var tabContent = $( tab.id + containerPostfix );
		    var tabContainer = $( tabContainerName );
		    if ( !tab.hasClassName("selectedTab")){
		    	// close other divs.
			    	for( var i = 0; i < tabContainer.childNodes.length; i++ ) {
			    		var target = tabContainer.childNodes[i];
			    		if( target.tagName == "LI"	&&  Element.hasClassName( target, "selectedTab") ) {
							
			    			Element.removeClassName( target, "selectedTab" );
			    			toggleDisp( $( target.id + containerPostfix ) );
			    			
			    		}
			    	}
			    	// open selected div
			    	
			        toggleDisp( tabContent );
			        
			        tab.addClassName("selectedTab");
		    }
	}