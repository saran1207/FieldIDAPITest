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
    jQuery("#searchText").bind("autocompleteopen", function(event, ui) {
        /* Position the popup menu so its right edge matches the right edge of the search box container */
        var menuWidth = jQuery('.ui-autocomplete:visible').outerWidth();
        var rightEdge = jQuery('#smartSearchContainer').offset().left + jQuery('#smartSearchContainer').outerWidth();
        var popupLeftEdge = rightEdge - menuWidth;
        jQuery('.ui-autocomplete:visible').css('left', popupLeftEdge);
    });
});

function displayString (item) {
   return "<a class='notranslate'>" + item.assetType + " : " + "<span class='strong'>" + item.label + "</span> / RIFD: " + item.rfidNumber + " / REF: " + item.customerRefNumber + "</a>";
}