/**
 */

var autoCompleteOrgPicker = (function() {

    /*
     * public methods exposed.
     */
    var render = function(ul, item) {
            return $( '<li></li>' )
                .data(  'item.autocomplete', item )
                .append( categoryFor(item) + linkFor(item) )
                .appendTo( ul );
    };


    var init = function(id, extraWidth) {
        $("#"+id).bind("autocompleteopen", function(event, ui) {
            var menu = $(".ui-autocomplete.ui-menu");
            var width = menu.width();
            menu.width(width+extraWidth);
        });
    };


    /**
     *  private data & functions
     */
    function linkFor(item) {
        var label = item.label;
        var start = item.matchStart;
        var prefix = '';
        var suffix = '';
        var end = item.matchStart + item.matchCount;

        if (item.matchStart!=-1) {
            prefix = item.label.substring(0, start);
            label = '<span class="matched">' + item.label.substring(item.matchStart,end) + '</span>';
            suffix = item.label.substring(end);
        }

        return '<a class="link">' + prefix + label + suffix + '</a>';
    }

    function categoryFor(item) {
        return '<span class="category">' + item.category + '</span>';
    }


    return {
        render : render,
        init : init
    };


})();




