/*
 *-----------------------------------------------------------------------------
 * Copyright (c) 2011-2014 FieldID - All Right Reserved
 *
 * Author: David Addison
 * File Version: 0.1
 *
 * This file can not be copied and/or distributed without the express
 * permission of FieldID
 *-----------------------------------------------------------------------------
 */
;(function($, window, document, undefined) {
    "use strict";

    var plugin   = 'pages';
    var version  = '0.1';
    var defaults = {
        // Flags
        debug: false,

        // Content container
        threshold: 0, // Subtract this many extra pixels from height check
        offset:    0, // Make page content container smaller by this many pixels

        // Page structure
        append:    'print-container',
        source:    'source',
        copy:      'copy',
        header:    'page-header',
        footer:    'page-footer',
        content:   'page-content',
        item:      'page-content-item',
        table:     'table',
        printbar:  'print-bar',
        page: {
            _class: 'print-page',
            current: {
                size:       'letter',
                orientation:'portrait'
            },
            preset: [
                { size: 'letter', orientation: 'portrait',  width: 816,  height: 1020 },
                { size: 'letter', orientation: 'landscape', width: 1020, height: 770  },

                { size: 'legal',  orientation: 'portrait',  width: 816,  height: 1370 },
                { size: 'legal',  orientation: 'landscape', width: 1370, height: 790  },

                { size: 'a4',     orientation: 'portrait',  width: 816,  height: 1180 },
                { size: 'a4',     orientation: 'landscape', width: 1180, height: 805  },

                { size: 'a3',     orientation: 'portrait',  width: 1040, height: 1460 },
                { size: 'a3',     orientation: 'landscape', width: 1460, height: 990  }
            ]
        },
        annotation: {
            _class: 'annotation',
            data: [
                { 'id': 'id12a',
                    'annotation': [
                        { 'x': 0.387097,
                            'y': 0.590909,
                            'text':'P-1',
                            'cssStyle':'pneumatic'
                        }
                    ]
                }
            ]
        },
    };

    //------------------------------------------------------------------[ Helper Functions
    // Convert params to classes
    function _class(){
        var result = '';
        var delim  = arguments[0]

        for(var i = 1; i < arguments.length; i++){
            result += '.' + arguments[i] + delim;
        }
        return result;
    };
    //------------------------------------------------------------------[ Plugin
    function Plugin(element, params){
        this.variables(params);
        this.convertIdToClass();
        this.head();
        this.setDefaultPage();
        this.printbar();

        this.run();
    };
    Plugin.prototype.variables = function(params){
        //---- store params
        this.params = $.extend({}, defaults, params);
        this.params.sourceFull = (this.params.page._class + '.' + this.params.source);
        this.params.copyFull   = (this.params.page._class + '.' + this.params.copy);

        //---- store commonly used selectors
        this.$header  = $( _class(' ', this.params.sourceFull, this.params.header));
        this.$footer  = $( _class(' ', this.params.sourceFull, this.params.footer));
        this.$content = $( _class(' ', this.params.sourceFull, this.params.content));
        this.$src     = $( _class(' ', this.params.sourceFull));
        this.$append  = $( _class(' ', this.params.append));
        this.$items   = $( _class(' ', this.params.item));
        this.$currentPage  = undefined;
        this.$newContainer = undefined;

        //---- store page count
        this.pageCount = 0;

        //---- Debugging output
        if(this.params.debug){
            console.log('----[ selectors ]----');
            console.log(this.$header);
            console.log(this.$footer);
            console.log(this.$content);
            console.log(this.$src);
        }
    };
    Plugin.prototype.measurements = function(){
        this.$src.show();

        //---- get page dimensions
        this.measure = {
            src: {
                width:  this.$src.width(),
                height: this.$src.height()
            },
            header: {
                width:  this.$header.width(),
                height: this.$header.height()
            },
            content: {
                width:  this.$content.width(),
                height: this.$content.height()
            },
            footer: {
                width:  this.$footer.width(),
                height: this.$footer.height()
            }
        };

        //---- Calculate the remaining area on the page for content (repeating header/footer)
        this.contentHeight = this.measure.src.height - this.measure.header.height - this.measure.footer.height - this.params.offset;

        //---- Debugging output
        if(this.params.debug){
            console.log('----[ measurements ]----');
            console.log('page: ' + this.measure.src.width + '-' + this.measure.src.height);
            console.log('header: ' + this.measure.header.width + '-' + this.measure.header.height);
            console.log('footer: ' + this.measure.footer.width + '-' + this.measure.footer.height);
            console.log('content: ' + this.measure.content.width + '-' + this.measure.content.height);
            console.log('contentHeight: ' + this.contentHeight);
        }

        this.$src.hide();
    };

//---- Functions to reflow pages
    Plugin.prototype.startPage = function(){
        //---- Create Page
        this.$currentPage = $('<div></div>');
        this.$currentPage.addClass(this.params.page._class);
        this.$currentPage.addClass(this.params.copy);
        this.$currentPage.addClass(this.params.page.current.size);
        this.$currentPage.addClass(this.params.page.current.orientation);
        this.$currentPage.appendTo(this.$append);

        //---- Copy header
        this.$header.clone().appendTo(this.$currentPage);

        //---- Add new content container
        this.$newContainer = $('<div></div>');
        this.$newContainer.addClass(this.params.content);
        this.$newContainer.appendTo(this.$currentPage);
    };
    Plugin.prototype.endPage = function(){
        this.$footer.clone().appendTo(this.$currentPage);
    };
    Plugin.prototype.appendToPage = function(elem){
        return $(elem).appendTo(this.$newContainer);
    };
    Plugin.prototype.checkForOverFlow = function(){
        if(this.params.debug){
            console.log('Current Height:' + this.$newContainer.outerHeight(true));
        }

        return this.$newContainer.outerHeight(true) > (this.contentHeight - this.params.threshold);
    };
    Plugin.prototype.pages = function(){
        //---- Store this for referencing in each()
        var tThis = this;

        //---- If needed, split items over pages
        this.startPage();

        $(this.$items).each(function(index, elem){
            if( $(elem).hasClass('table') ){
                tThis.splitTable(elem);

            } else if( $(elem).hasClass('image-group') ){
                tThis.splitImageGroup(elem);

            } else {

                // Add element to current page
                tThis.appendToPage(elem);

                // If the element overflows the page create a new page
                // and move the elment to the new page
                if( tThis.checkForOverFlow() ){
                    if(tThis.params.debug){
                        console.log('Starting new page');
                    }
                    tThis.endPage();
                    tThis.startPage();

                    // Re-append element to new page
                    tThis.appendToPage(elem);
                }
            }
        });

        this.endPage();                                                   // Add footer to last page
        var cfc = _class(' ',this.params.copyFull, this.params.content);  // Resize the container to fit the page
        $(cfc).height(this.contentHeight);
    };
    Plugin.prototype.isElementEmpty = function(el){
        return !$.trim(el.html())
    };
    Plugin.prototype.splitTable = function(elem){
        var tThis = this;

        //---- Get table and table items
        var $table = $(elem).find('table');
        var $items = $table.find('tbody tr');

        //---- Move table items into a temp container
        var $temp = $('<div></div>');
        $temp.append($items);

        //---- Clone section+table+header and add it to the current page
        var $chunk = $(elem);
        $chunk.clone().appendTo(this.$newContainer);

        //---- get tbody for appending items to
        var $currentTable = this.$newContainer.find('tbody');

        //---- If needed, split table over multiple pages
        $items.each(function(index, elem){

            // Add element to current table
            $(elem).appendTo($currentTable);

            // If the element overflows the page create a new table & page
            // and move the element to the new table
            if( tThis.checkForOverFlow() ){

                // Store the element to check if table is empty
                var store = $('<div></div>');
                $(elem).appendTo(store);

                // If the table is empty remove the whole the whole section
                // This prevents orphaned table headers
                if( tThis.isElementEmpty($currentTable) ){
                    $currentTable.closest('.table').remove();
                }

                if(tThis.params.debug){
                    console.log($currentTable);
                    console.log('Starting new table');
                }
                tThis.endPage();
                tThis.startPage();

                // Create new table
                $chunk.clone().appendTo(tThis.$newContainer);
                $currentTable = tThis.$newContainer.find('tbody');

                // Re-append element to new table
                $(elem).appendTo($currentTable);
            }
        });
    };
    Plugin.prototype.splitImageGroup = function(elem){
        console.log('split image group');
        var tThis = this;

        //---- Get images
        var $items = $(elem).find('.images');
        console.log($items);

        //---- Move images into a temp container
        var $temp = $('<div></div>');
        $temp.append($items);

        //---- Clone section and add it to the current page
        var $chunk = $(elem);
        var $currentGroup = $chunk.clone() // Get current image-group to append items to
        $currentGroup.appendTo(this.$newContainer);

        //---- If needed, split images over multiple pages
        $items.each(function(index, elem){

            // Add element to current table
            $(elem).appendTo($currentGroup);

            // If the element overflows the page create a new group & page
            // and move the element to the new iamge group
            if( tThis.checkForOverFlow() ){

                if(tThis.params.debug){
                    console.log('Starting new image-group');
                }
                tThis.endPage();
                tThis.startPage();

                // Create new image group
                $currentGroup = $chunk.clone();
                $currentGroup.appendTo(tThis.$newContainer);

                console.log($currentGroup);

                // Re-append element to new group
                $(elem).appendTo($currentGroup);
            }
        });
    };

//----- Functions for different page sizes and updating page size
    Plugin.prototype.updateVariable = function(elem, str){
        var temp = $(elem).attr('class').replace(str+' ', '');
        this.params.page.current[str] = temp;
    };
    Plugin.prototype.updateSrc = function(){
        //---- Remove all classes and add back required classes
        this.$src.removeClass();
        this.$src.addClass(this.params.page._class);
        this.$src.addClass(this.params.source);
        this.$src.addClass(this.params.page.current.size);
        this.$src.addClass(this.params.page.current.orientation);
    };
    Plugin.prototype.head = function(){
        //---- Add page sizes to head
        var style  = '<style>\n';

        var length = this.params.page.preset.length;
        for(var i = 0; i < length; i++){
            style += '\t.' + this.params.page._class;
            style += _class('', this.params.page.preset[i].size);
            style += _class('', this.params.page.preset[i].orientation);
            style += '{'
            style += 'width:'  + this.params.page.preset[i].width  + 'px;';
            style += 'height:' + this.params.page.preset[i].height + 'px;';
            style += '}\n';
        }
        style += '</style>\n';
        $('head').append(style);

        //---- Debugging output
        if(this.params.debug){
            console.log('----[ css for page sizes ]----');
            console.log(style);
        }
    };
    Plugin.prototype.setDefaultPage = function(){
        var pg = this.params.page.current;
        $('.' + this.params.sourceFull).addClass(pg.size);
        $('.' + this.params.sourceFull).addClass(pg.orientation);
    };

    // Bind click handlers for page resize
    Plugin.prototype.printbar = function(){
        //TODO: Change print bar trigger to reflect changes to the params structure
        // old params page.size =[] page.orientation = [] to preset = []

        // var tThis = this;

        // for(var key in this.params.page.preset){
        //     if(this.params.page.preset.hasOwnProperty(key)){

        //         var selector = _class(' ',this.params.page._class, key);

        //         $(selector).each(function(index, elem){
        //             $(elem).click( {'key': key}, function(ev){
        //                 tThis.updateVariable(elem, ev.data.key);
        //                 tThis.updateSrc();
        //                 tThis.run();
        //             });
        //         });
        //     }
        // }
    };

//---- Functions for image annotation
    Plugin.prototype.annotate = function(){
        var notes = this.params.annotation;

        // Remove all annotations if they exist
        $(_class(' ', notes._class)).remove();

        for( var i = 0; i < notes.data.length; i++){
            this.renderAnnotationSet( notes.data[i]);
        }
    };
    Plugin.prototype.renderAnnotationSet = function(set){
        var tThis = this;

        // For every element that matches the class
        // Render the set of annotations
        $('.'+set.id).each(function(index, elem){
            for(var i = 0; i < set.annotation.length; i++){
                tThis.renderAnnotation(elem, set.annotation[i]);
            }
        });
    };
    Plugin.prototype.renderAnnotation = function(elem, annotation){
        //TODO: use params to form classes / elements
        // ex. this.params.annotation._class

        var container = $(elem)
        var containerWidth  = $(container).width();
        var containerHeight = $(container).height();
        var direction = annotation.x < 0.5 ? 'arrow-left' : 'arrow-right';

        // Add notes-container if it isn't present
        if( !container.parent().hasClass('notes-container') ){
            container.wrap('<div class="notes-container"></div>')
        }

        // Generate HTML for the annotation
        var element = $('<span class="annotation readonly note '+ direction + ' ' + annotation.cssStyle + '"></span>');
        $('<span class="icon"></span>').appendTo(element);
        $('<input/>').attr({type:'text', value:annotation.text}).attr('disabled',true).appendTo(element);


        element.css({position: 'absolute'});
        container.after(element);

        // position the annotation (this must happen after adding the element)
        var left = (annotation.x * containerWidth);
        var top  = (annotation.y * containerHeight) - ($(element).height() / 2);
        element.css({ left: left+'px', top: top+'px'});
    };
    Plugin.prototype.convertIdToClass = function(){
        $('img').each(function(index,elem){
            var id = $(elem).attr('id');
            $(elem).addClass(id);
            $(elem).removeAttr('id');
        });
    };

//---- Main task runner
    Plugin.prototype.run = function(){
        //---- Join table and add it back to source page
        // this only allows to one table rigth now. This will break two or more tables
        var from = _class(' ', this.params.copy,   this.params.table) + 'tbody tr';
        var to   = _class(' ', this.params.source, this.params.table) + 'tbody';
        $(from).appendTo(to);

        //---- Move back to source and remake pages
        $(this.$items).appendTo(this.$content);        // Move pages back to source
        $(_class('',this.params.copyFull)).remove();   // Remove copy pages
        this.measurements();                           // Check for measurements changes
        this.pages();                                  // Make pages
        this.annotate();
    };
    Plugin.prototype.destroy = function(){
        this.$el.removeData();
    };

    $.fn[plugin] = function(params){
        return this.each(function(){
            if(!$.data(this,"plugin_"+plugin)){
                $.data(this,"plugin_"+plugin,new Plugin(this,params));
            }
        });
    };

})(jQuery, window, document);