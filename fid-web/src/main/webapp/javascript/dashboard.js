/**
+ * requires.... 
 *   jquery.flot.js 
 *  
 */

var dashboardWidgetFactory = (function() { 
	
	// instead of passing id, why not pass reference to element???
	var create = function(id) { 
		var widget = dashboardWidget(id);
	    widget.setTooltips(true);
		return widget;
	};
	
	var createWithData = function(id,data) {
		var widget = create(id);
		widget.update(data);
		return widget;
	}	
	
	var previousPoint = null;
	
			
	/**
	 * widget object returned by factory 
	 */	
	function dashboardWidget(widgetId) {

		/* private methods and properties */
		var id = widgetId;

		// private method
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
		}
		
		// TODO : refactor this out of here...should be part of options.
		function formatDate(d) { 
			var day = d.getDate();
			var month = d.getMonth();
			var year = d.getFullYear();
			return options.xaxis.monthNames[month] + ' ' + day + ' ' + year; 
		}
		
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
		}		
		
		/* public methods exposed */
		return { 	
			setTooltips : function(tooltips) { 
				$('#'+id).unbind("plothover");
				if (tooltips) { 
					bindTootlips(id);
				}
			},
			update : function(newData, text) {
				var d1 = [{data:newData, label:text}];
			    $.plot($('#'+id), d1, options);				
			},
			options : options = {
			        lines: { show: true },
			        points: { show: true },
			        selection: { mode: "x" },
			        yaxis : { 
			        	decimals: "0"
			        },
			        xaxis: {
			        	dateFormatter : formatDate,
			        	mode: "time",
						timeformat : "%b %d, %y",			
						monthNames : ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
			        	},
			        grid: { hoverable: true, clickable: true }
			    },						
		};
		
	};
			
	return { 
		create : create,
		createWithData : createWithData,
	};

	
})();



		
