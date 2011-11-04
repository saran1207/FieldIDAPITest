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
		var me = $('#'+widgetId);		
		
		function setup() { 
			configButton = $('#'+id).find('.widget-buttons').find('.configButton');					
			configButton.click(function() { toggleConfig(); });
			configContainer = $('#'+id).find('.configContainer');
			config = configContainer.find('.config');
			config.css({
				marginLeft : 600
			});						
		}
		
		function toggleConfig() { 
			configContainer = $('#'+id).find('.configContainer');
			config = configContainer.find('.config');
			slideOut = parseInt(config.css('marginLeft'),10)==0;
			finish = function() {};
			config.show();
			if (slideOut) { 
				configContainer.css({
					overflow : 'hidden'
				});
			} else { 
				finish = function() {
					configContainer.css({
						overflow : 'visible'
					});					
				}
			}
			config.animate({
					marginLeft: slideOut ? config.outerWidth()+5 : 0
				},
				'fast', 'linear', finish
			);
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



		
