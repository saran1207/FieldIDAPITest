jQuery(document).ready(function() {
    jQuery("#searchText").autocomplete({
        minLength: 3,
        source: function( request, response ) {
            jQuery.getJSON('json/smartList.action', {
                term: request.term
            }, function (data) {
                // data is an array of objects and must be transformed for autocomplete to use
                var array = data.error ? [] : jQuery.map(data.assetList, function (item) {
                    return {
                        label: item.identifier,
                        id   : item.id,
                        rfidNumber: item.rfidNumber,
                        customerRefNumber: item.customerRefNumber,
                        assetType: item.assetType
                    };
                });
                response(array);
            });
        },
        select:function(event, ui) {
            window.location.href = "w/assetSummary?uniqueID=" +  ui.item.id
        }
    }).data( "autocomplete" )._renderItem = function( ul, item ) {
        return jQuery( "<li></li>" )
            .data( "item.autocomplete", item )
            .append(displayString(item))
            .appendTo( ul );
    };
});

function displayString (item) {
   return "<a>" + item.assetType + " : " + "<span class='strong'>" + item.label + "</span> / RIFD: " + item.rfidNumber + " / REF: " + item.customerRefNumber + "</a>";
}