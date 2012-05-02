/**
 */

var autoCompleteOrgPicker = (function() {

    /*
     * public methods exposed.
     */
    var init = function(id, extraWidth) {
        $("#"+id).bind("autocompleteopen", function(event, ui) {
            var menu = $(".ui-autocomplete.ui-menu");
            var width = menu.width();
            menu.width(width+extraWidth);
            // customize our tooltips.
            $('.link').tipsy({gravity: 'e'});
        });
    };

    var render = function(ul, item) {
            return (item.descClass=='no-results') ?
                        renderNoResults(ul, item) :
                    (item.descClass=='max-results') ?
                        renderMaxResults(ul,item) :
                        renderItem(ul, item);
    };

    function renderItem(ul,item) {
        return renderImpl(ul, item, descFor(item) + linkFor(item));
    }

    function renderNoResults(ul,item) {
        ul.width('200px');
        ul.css('border-left','0px');
        return renderImpl(ul, item,  '<span class="'+item.descClass+'">' + item.desc + '</span>' );
    }

    function renderMaxResults(ul, item) {
        return renderImpl(ul, item, descFor(item));
    }

    function renderImpl(ul, item, anchor) {
        return $( '<li></li>' )
            .data(  'item.autocomplete', item )
            .append( anchor )
            .appendTo( ul );
    }

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

        return '<a title="' + item.tooltip + '" class="link">' + prefix + label + suffix + '</a>';
    }

    function descFor(item) {
        return item.desc ?
            '<span class="'+item.descClass+'">' + item.desc + '</span>' :
            '';
    }

    return {
        render : render,
        init : init
    };


})();




