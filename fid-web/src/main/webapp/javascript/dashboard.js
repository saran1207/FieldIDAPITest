/**
 * requires.... 
 *   jquery.flot.js
 *   jquery.flot.stack.js
 *   jquery.flot.navigate.js
 * @see FlotChart for java class that uses this.   
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
			                	showTooltip(item.pageX, item.pageY, tooltipContent(item, options));
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
		
	var dateTooltipContent = function(item, options) {
		var datapoint = item.datapoint;
		var y = datapoint[1].toFixed(options.yaxis.tickDecimals);
        var map = createTooltipVariablesMap(datapoint, options);
        var tooltip = formatTooltip(map, options.tooltipFormat);
	    return tooltip;
	};
	
	var horizLabelTooltipContent = function(item, options) {
		var datapoint = item.datapoint;
		var value = datapoint[0].toFixed(0);
	    var index = datapoint[1].toFixed(0);
		var seriesIndex = item.seriesIndex;
	    var label = options.yaxis.ticks[index][1];
	    if (options.yaxis.ticks[index][2+seriesIndex]) {
	    	return options.yaxis.ticks[index][2+seriesIndex];
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
    
    function bindClick(id,options) {    	  
    	$('#'+id).bind("plotclick", function(event,pos,item) {
    		if (item && isClickableSeries(item)) {   // generate URL & parameters via options and arguments.
    			window.location=clickUrl(options, event, pos, item);
    		}
    	});
    }

	function isClickableSeries(item) {
		// hack : for now, the ALL series in the event completeness widget will not be clickthru even though the other series will be.
		// currently, flot doesn't allow series by series disabling of clickthru so we have to do this.
		if (item.series) {
			if (item.series.lines) {
				if ('clickable' in item.series.lines) {
					return item.series.lines.clickable;
				}
			}
		}
		return true;
	}

	function clickUrl (options, event, pos, item) {
		var transposed = options.bars.horizontal;
		var xIndex = transposed?1:0;   // account for possibility that X&Y axis flip flopped?
		var yIndex = transposed?0:1;
		
		var url = options.fieldIdOptions.url;
		//  longX
		url += "&longX="+item.datapoint[xIndex];
		//  y
		if(options.bars.show) {  // for horizontal bar charts we pass the label, not the numeric value.
			var index = item.datapoint[1];
			url+="&y="+(transposed?item.series.yaxis.ticks[index].label:item.series.xaxis.ticks[index].label); 
		} else {
			url+="&y="+item.datapoint[yIndex];
		}
		// series
		if (typeof item.series.id=='string') {
            url += "&series="+item.series.id;
        } else if (item.series.id && item.series.id.label) {
            url += "&series="+item.series.id.label;
		} else { 
			url+="&series="+item.seriesIndex;			
		}			
		return url;
	}

	function getLabel(bar) {
		var type = getBarType(bar);
		$(bar).parent().find()
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
		if (options.fieldIdOptions.clickable) {
			bindClick(id,options);
		}
		widget.update(data);
		return widget;
	};

	var updateKpi = function() {
		var delay = 250;
		$('.eventKpiWidget').find('.southwest[title]').tipsy({gravity: 'sw', fade:true, delayIn:delay});
		$('.eventKpiWidget').find('.southeast[title]').tipsy({gravity: 'se', fade:true, delayIn:delay});
		$('.eventKpiWidget').find('.south[title]').tipsy({gravity: 's', fade:true, delayIn:delay});
		$('.eventKpiWidget').find('.northwest[title]').tipsy({gravity: 'nw', fade:true, delayIn:delay});
		$('.eventKpiWidget').find('.north[title]').tipsy({gravity: 'n', fade:true, delayIn:delay});
		$('.eventKpiWidget').find('.northeast[title]').tipsy({gravity: 'ne', fade:true, delayIn:delay});

		var getType = function(bar) {
			if (bar.hasClass('failed')) return 'failed'
			else if (bar.hasClass('na')) return 'na'
			else if (bar.hasClass('passed')) return 'passed'
			else if (bar.hasClass('closed')) return 'closed'
		}

		var highlightLabelFromBar = function(bar) {
			$(bar).parents('.kpi').find('.section.bottom .label .'+getType($(bar))).addClass('highlight');
		}

		var unHighlightLabelFromBar = function(bar) {
			$(bar).parents('.kpi').find('.section.bottom .label .'+getType($(bar))).removeClass('highlight');
		}

		$.each($('.eventKpiWidget .kpi'), function() {
			var kpi = $(this);
			$(this).find('.section.top .bars.completed').mouseenter(function() {
				kpi.find('.section.bottom').addClass('opened').animate({height:'20px'},200);
			});
			$(this).mouseleave(function() {
				kpi.find('.section.bottom').removeClass('opened').animate({height:'0px'},200);
			});
		});

		$.each($('.eventKpiWidget .kpi .section.bottom .label div'), function() {
			var label = $(this);
			$(this).mouseenter(function() {
				label.addClass('highlight');
			});
			$(this).mouseleave(function() {
				label.removeClass('highlight')
			});
		});

		$.each($('.eventKpiWidget .kpi .section.top .bar'), function() {
			$(this).mouseenter(function() {
				highlightLabelFromBar(this);
			});
			$(this).mouseleave(function() {
				unHighlightLabelFromBar(this);
			});
		});


	};


			
	return {		
		horizTooltip : horizLabelTooltipContent,
		dateTooltip : dateTooltipContent,
		clickUrl : clickUrl,
		updateKpi : updateKpi,
		create : create,
		createWithData : createWithData
	};

	
})();



		
