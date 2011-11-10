/**
 *     
 *   
 */

var widgetToolkit = (function() { 
	
	/**
	 * widget object returned by factory 
	 */	
	function widget(widgetId) {

		/* private methods and properties */
		var id = widgetId;
		var containerHeight = 0;		
		
		function setup() { 
			configButton = $('#'+id).find('.widget-buttons').find('.configButton');					
			configButton.click(function() { toggleConfig(); });
			configContainer = $('#'+id).find('.configContainer');
			config = configContainer.find('.config');
			config.css({
				marginLeft : 600,
				style : ''
			});			
		}
		
		function toggleConfig() { 
			configContainer = $('#'+id).find('.configContainer');
			config = configContainer.find('.config');
			hideConfig = parseInt(config.css('marginLeft'),10)==0;
			finish = function() { };
			config.show();
			if (hideConfig) {
                finish = function() { config.hide() };
			} else {
				containerHeight = Math.max(100,configContainer.height());				
			}
			maybeResizeContainer(hideConfig, config, configContainer);
			config.animate({
					marginLeft: hideConfig ? config.outerWidth()+5 : 0
				},
				'fast', 'linear', finish
			);		
		}
		
		function maybeResizeContainer(hideConfig, config, configContainer) {
			configHeight = config.css('maxHeight');
			// if height requires special height instead of just filling area of widget....			
			if (configHeight) { 
				if (hideConfig) { 
					configContainer.animate({height:containerHeight},
							'fast', 'linear', 
							function() {$(this).css({height:''}); }
						);
				} else {
					configContainer.animate({height:configHeight});
				}							
			}
		}
		
		/* public methods exposed */
		return {
			setup : setup
		};
		
	}			

			
	var registerWidget = function(id) {
		var w = widget(id);
		w.setup();
		return w;
	};

	
	return {		
		registerWidget : registerWidget
	};

	
})();



		
