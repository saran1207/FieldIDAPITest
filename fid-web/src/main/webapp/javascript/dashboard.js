/**
+ * requires.... 
 *   jquery.flot.js 
 *   jquery.flot.navigate.js
 */

var dashboardWidgetFactory = (function() { 
	
	/**
	 * widget object returned by factory 
	 */	
	function dashboardWidget(widgetId, x) {

		/* private methods and properties */
		var id = widgetId;
		var previousPoint = null;	
		var options = x;
		
		function bindTootlips(id) {		
			$('#'+id).bind("plothover", function (event, pos, item) {
			    $("#x").text(pos.x.toFixed(2));
			    $("#y").text(pos.y.toFixed(2));

			        if (item) {
			            if (previousPoint != item.datapoint) {
			                previousPoint = item.datapoint;
			                
			                $("#tooltip").remove();
			                var y = item.datapoint[1].toFixed(options.yaxis.decimals);
			                var date = formatDate(new Date(item.datapoint[0]));
			                showTooltip(item.pageX, item.pageY,  y + "<br>" + date);
			            }
			        }
			        else {
			            $("#tooltip").remove();
			            clicksYet = false;
			            previousPoint = null;            
			        }
				});
		};
		
		// TODO : refactor this out of here...should be part of options.
		function formatDate(d) { 
			var day = d.getDate();
			var month = d.getMonth();
			var year = d.getFullYear();
			return options.xaxis.monthNames[month] + ' ' + day + ' ' + year; 
		};
		
		function showTooltip(x, y, contents) {
		    $('<div id="tooltip">' + contents + '</div>').css( {
		        position: 'absolute',
		        display: 'none',
		        top: y + 5,
		        left: x + 5,        
		        border: '1px solid #fdd',
		        'text-align': 'center',
		        padding: '2px',
		        'background-color': '#fee',
		        opacity: 0.80
		    }).appendTo("body").fadeIn(200);
		};		
		
		/* public methods exposed */
		return { 	
			setTooltips : function(tooltips) { 
				$('#'+id).unbind("plothover");
				if (tooltips) { 
					bindTootlips(id);
				}
			},
			update : function(newData) {				
			    $.plot($('#'+id), newData, options);				
			}
		};
		
	}
	
	// instead of passing id, why not pass reference to element???
	var create = function(id) { 
		var widget = dashboardWidget(id);
		return widget;
	};
	
	var createWithData = function(id,data,options) {
		var widget = dashboardWidget(id,options);
		if(!options.yaxis.panRange) {
			options.yaxis.panRange=false;
		}
		if(options.grid.hoverable) { 
			widget.setTooltips(true);
		}
		widget.update(data);
		return widget;
	};	
	
			
	return { 
		create : create,
		createWithData : createWithData
	};

	
})();



		
