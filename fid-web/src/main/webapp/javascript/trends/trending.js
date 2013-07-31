var plainTrendsTooltip = function(item, options, total) {
    var dataIndex = item.dataIndex;
    var criteriaName = item.series.xaxis.ticks[dataIndex].label;
    var count = item.datapoint[1];
    return "<span class='count'>"+count+"</span>"+"<span class='criteriaName'>"+criteriaName+"</span>";
};