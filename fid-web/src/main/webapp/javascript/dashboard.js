/**
 * requires.... 
 *   jquery.flot.js 
 *   jquery.flot.navigate.js
 */

var chartWidgetFactory = (function() { 
	
	/** 
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

	/*
	 * private methods/vars for chartWidgetFactory.
	 */
	var MS_PER_DAY = 1000*60*60*24;
		
	var dateTooltipContent = function(datapoint, options) { 
	    var y = datapoint[1].toFixed(options.yaxis.tickDecimals);
        var map = createTooltipVariablesMap(datapoint, options);
        var tooltip = formatTooltip(map, options.tooltipFormat);
	    return tooltip;
	};
	
	var horizLabelTooltipContent = function(datapoint, options) {
	    var value = datapoint[0].toFixed(0);
	    var index = datapoint[1].toFixed(0);
	    var label = options.yaxis.ticks[index][1];
	    if (options.yaxis.ticks[index][2]) {
	    	return options.yaxis.ticks[index][2]; 
	    } else {
	    	return "<p>"+label + ": <b>" +value+ "</b></p>";
	    }
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
        var date2Obj = new Date(datapoint[0]+6*MS_PER_DAY);
        var y = datapoint[1].toFixed(options.yaxis.decimals);
        var weekEndDay = '';
        if (date2Obj.getUTCMonth()!=dateObj.getUTCMonth()) {
        	weekEndDay = monthNames[date2Obj.getUTCMonth()] + ' ';
        }
        weekEndDay += date2Obj.getUTCDate();
        return {
            year: dateObj.getUTCFullYear(),
            month: monthNames[dateObj.getUTCMonth()],
            day: dateObj.getUTCDate(),
            weekEndDay : weekEndDay,
            y: y
        };
    }
	
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



		
