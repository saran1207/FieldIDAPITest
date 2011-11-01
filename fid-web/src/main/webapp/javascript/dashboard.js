/**
 * requires.... 
 *   jquery.flot.js 
 *   jquery.flot.navigate.js
 */

var dashboardWidgetFactory = (function() { 
	
	/**
	 * widget object returned by factory 
	 */	
	function dashboardWidget(widgetId, opts) {

		/* private methods and properties */
		var id = widgetId;
		var previousPoint = null;	
		var options = opts;
		var tooltipContent = null; 
		
		function bindTootlips(id) {
			$('#'+id).bind("plothover", function (event, pos, item) {
			        if (item) {
			            if (previousPoint != item.datapoint) {
			                previousPoint = item.datapoint;
			                
			                $("#tooltip").remove();
			                if (tooltipContent) {
			                	showTooltip(item.pageX, item.pageY, tooltipContent(item.datapoint, options));
			                }
			            }
			        }
			        else {
			            $("#tooltip").remove();
			            clicksYet = false;
			            previousPoint = null;            
			        }
				});
		};
					
		function showTooltip(x, y, contents) {
		    $('<div id="tooltip" class="chartTooltip">' + contents + '</div>').css( {
		        top: y + 5,
		        left: x + 5
		    }).appendTo("body").fadeIn(200);
		};		
		
		/* public methods exposed */
		return { 	
			setTooltip : function(fn) { 
				tooltipContent = fn;
			},
			showTooltips : function(show) { 
				$('#'+id).unbind("plothover");
				if (show) {	bindTootlips(id);}
			},
			update : function(newData) {				
			    $.plot($('#'+id), newData, options);				
			}
		};
		
	}			

	function formatDate(d,months) { 
		var day = d.getDate();
		var month = d.getMonth();
		var year = d.getFullYear();
		return months[month] + ' ' + day + ' ' + year; 
	};
	
	var dateTooltipContent = function(datapoint, options) { 
	    var y = datapoint[1].toFixed(options.yaxis.decimals);
	    var date = formatDate(new Date(datapoint[0]), options.xaxis.monthNames);
	    return date + ": " + y;
	};
	
	var horizLabelTooltipContent = function(datapoint, options) {
		// assumes a 0...N list.  otherwise i would have to search ticks list for index.
	    var value = datapoint[0].toFixed(0);
	    var index = datapoint[1].toFixed(0);
	    var label = options.yaxis.ticks[index][1];
	    return label + " : " + value;		
	};
	
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
		widget.showTooltips(options.grid.hoverable);
		if (options.xaxis.mode == "time") {
			widget.setTooltip(dateTooltipContent);
		} else if (options.bars.horizontal) {
			widget.setTooltip(horizLabelTooltipContent);
		}
		widget.update(data);
		return widget;
	};	
	
			
	return { 
		horizTooltip : horizLabelTooltipContent,
		dateTooltip : dateTooltipContent,
		create : create,
		createWithData : createWithData
	};

	
})();



		
