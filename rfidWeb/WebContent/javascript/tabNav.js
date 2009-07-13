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
	    try {
		    var tabContainer = $( tabContainerName );
		    if ( !tab.hasClassName("selectedTab")){
		    	// close other divs.
		    	try {
			    	for( var i = 0; i < tabContainer.childNodes.length; i++ ) {
			    		var target = tabContainer.childNodes[i];
			    		try{
			    		if( target.tagName == "LI"	&&  Element.hasClassName( target, "selectedTab") ) {
							try{
			    			Element.removeClassName( target, "selectedTab" );
			    			} catch ( e ) {
			    				alert('3');
			    				}
			    			try{
			    			toggleDisp( $( target.id + containerPostfix ) );
			    			} catch( e ) {
			    				alert( '4');
			    			}
			    		}
			    		} catch ( e ) {
			    			
			    			alert('55');
			    		}
			    	}
			    	// open selected div
			    	
			        toggleDisp( tabContent );
			        
			        tab.addClassName("selectedTab");
			    } catch (p ){
			      	alert( 'woo' );
			    }
		    }
	    } catch( e) {
	    	alert( e );
	    }
	}