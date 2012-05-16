/**
 * usage notes : expects a json object= item {
 *    label : the text for the item it
 *    tooltip : the "title" html element.
 *    matchStart :
 *    matchCount :
 *    desc :
 */

var autoCompleter = (function() {

    var descriptionWidth = 120;
    var fudgeFactor = 10;

    /*
     * public methods exposed.
     */
    var init = function(id) {
        var auto = $("#"+id);
        auto.initialized=false;
        auto.bind("autocompleteopen", function(event, ui) {
            if (!auto.initialized) {
                var menu = $('.ui-autocomplete:visible');
                menu.css('width',menu.width()+10); // add room because we've styled it with extra padding between description and result columns.
            }
            auto.initialized=true;
            $('.ui-autocomplete .link').tipsy({gravity: 'e', fade:true, delayIn:150});
            // hack to remove jquery styling because rounded corners mucks up the border.
            $('.ui-autocomplete').removeClass('ui-corner-all');
        });
        auto.bind("autocompleteclose", function(event, ui) {
            $("#"+id).focus();
        });
    };

    var render = function(ul, item) {
        return (item.descClass=='no-results') ?
                    renderNoResults(ul, item) :
                (item.descClass=='max-results') ?
                    renderMaxResults(ul,item) :
                    renderItem(ul, item);
    };

    var closeOnScroll = function(element) {
        $(element).scroll(function() {
            // bug fix...autocomplete menu should hide when mouse wheel (or other scrolling) happens.  if it's in a scrollable container then
            // you should probably hook this up.  otherwise the menu will just sit there when you scroll the associated text field away.
            $('.ui-autocomplete:visible').hide();
        });
    };

    function renderItem(ul,item) {
        return renderImpl(ul, item, descFor(item) + linkFor(item));
    }

    function renderNoResults(ul,item) {
        ul.width(100);
        return renderImpl(ul, item,  descFor(item) );
    }

    function renderMaxResults(ul, item) {
        return renderImpl(ul, item, descFor(item), 'ui-menu-item max-results-container');
    }
    
    function renderImpl(ul, item, anchor, cssClass) {
        var li = '<li></li>';
        if (cssClass) {
            li = '<li class="' + cssClass + '"></li>';
        }
        return $(li)
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
        closeOnScroll : closeOnScroll,
        init : init
    };


})();




