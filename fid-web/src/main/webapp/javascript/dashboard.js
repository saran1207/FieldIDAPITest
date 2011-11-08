/**
 * requires.... 
 *   jquery.flot.js 
 *   jquery.flot.navigate.js
 */

var chartWidgetFactory = (function() { 
	
	/**   TODO DD : rename this to ChartWidgetFactory....createChart etc...
	 * widget object returned by factory 
	 */	
	function chartWidget(widgetId, opts) {

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

	var dateTooltipContent = function(datapoint, options) { 
	    var y = datapoint[1].toFixed(options.yaxis.decimals);
        var map = createTooltipVariablesMap(datapoint, options);
        var tooltip = formatTooltip(map, options.tooltipFormat);
	    return tooltip;//"<p>"+date + ": <b>" +y+ "</b></p>";
	};
	
	var horizLabelTooltipContent = function(datapoint, options) {
		// assumes a 0...N list.  otherwise i would have to search ticks list for index.
	    var value = datapoint[0].toFixed(0);
	    var index = datapoint[1].toFixed(0);
	    var label = options.yaxis.ticks[index][1];
	    return "<p>"+label + ": <b>" +value+ "</b></p>";
	};

    function formatTooltip(map, tooltipFormat) {
        var tooltip = tooltipFormat;
        for (var key in map) {
            var reg = new RegExp("\\{" + key + "\\}", "gm");
            tooltip = tooltip.replace(reg, map[key]);
        }
        return tooltip;
    }

    function createTooltipVariablesMap(datapoint, options) {
        var monthNames = options.xaxis.monthNames;
        var dateObj = new Date(datapoint[0]);
        var y = datapoint[1].toFixed(options.yaxis.decimals);
        var index = datapoint[1].toFixed(0);
        return {
            year: dateObj.getFullYear(),
            month: monthNames[dateObj.getMonth()],
            day: dateObj.getDate(),
            y: y
        };
    }
	
	// instead of passing id, why not pass reference to element???
	var create = function(id) { 
		var widget = chartWidget(id);
		return widget;
	};
	
	var createWithData = function(id,data,options) {
		var widget = chartWidget(id,options);
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



		
