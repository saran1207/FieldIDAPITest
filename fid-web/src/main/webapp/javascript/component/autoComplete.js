/**
 * usage notes : expects a json object= item {
 *    label : the text for the item it
 *    tooltip : the "title" html element.
 *    matchStart :
 *    matchCount :
 *    desc :
 */

var autoCompleter = (function() {

    /*
     * public methods exposed.
     */
    var init = function(id) {
        $("#"+id).bind("autocompleteopen", function(event, ui) {
            $('.link').tipsy({gravity: 'e', fade:true, delayIn:150});
            $('.ui-autocomplete').removeClass('ui-corner-all');
        });
        $("#"+id).bind("autocompleteclose", function(event, ui) {
            $("#"+id).focus();
        });
    };

    var render = function(ul, item) {
        ul.removeClass('empty');
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
        ul.addClass('empty');
        return renderImpl(ul, item,  descFor(item) );
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
        if (item.matchStart>=0) {
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




