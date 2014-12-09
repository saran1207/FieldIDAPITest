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
;(function($, window, document, undefined){
    "use strict";

    var plugin = 'arrowAnnotationEditor';
    var defaults = {
        apiUrl: 'url', // MANDATORY! - Url for AJAX

        arrow: {
            head: {'x':null, 'y':null},
            tail: {'x':null, 'y':null}
        }
    };
    
    function Plugin(element, params){
        this.editor = $.extend({}, defaults, params);
        this.editor.arrow.element = null;
        this.editor.arrow.size = 5;
        this.editor.arrow.color = '#ff0000';
        this.editor.index = 0;
        this.editor.state = ['empty', 'headSelected', 'tailSelected'];

        this.element = element;
        this.width   = $(element).width();
        this.height  = $(element).height();
        

        // Check for existing arrow values
        if( this.editor.arrow.head.x !== null &&
            this.editor.arrow.head.y !== null &&
            this.editor.arrow.tail.x !== null &&
            this.editor.arrow.tail.y !== null ){
            
            // Update editor state
            this.editor.index = this.editor.state.length -1;
            this.renderArrow();
        }
        
        // Set arrow head color (can't seem to limit the scope to the calling element, so using 'document')
        var poly = this.element.getElementsByClassName('arrowAnnotationEditor-arrow-poly');
        for(var i = 0, len = poly.length; i < len; i++ ){
            poly[i].setAttributeNS(null, 'fill', this.editor.arrow.color);
        }
        
        $(element).on('mousemove', $.proxy(this.move,   this));
        $(element).on('mousedown', $.proxy(this.action, this));
    };

    // Track mouse move after the head point has been selected
    Plugin.prototype.move = function(event){
        if( this.editor.state[ this.editor.index] === 'headSelected' ){
            // Store tail based on current mouse position
            this.editor.arrow.tail.x = this.width  / event['offsetX'];
            this.editor.arrow.tail.y = this.height / event['offsetY'];

            if( this.editor.arrow.element !== null ){
                this.editor.arrow.element.setAttributeNS(null, 'x2', event['offsetX']);
                this.editor.arrow.element.setAttributeNS(null, 'y2', event['offsetY']);
            }
        }
    };
    
    // State machine for tracking (click) events
    Plugin.prototype.action = function(event){

        // Update editor state
        this.editor.index = ++this.editor.index % this.editor.state.length;

        switch( this.editor.state[this.editor.index] ){
                
            case 'empty':
                // Clear internal data structure
                this.editor.arrow.head.x = null;
                this.editor.arrow.head.y = null;
                this.editor.arrow.tail.x = null;
                this.editor.arrow.tail.y = null;

                // Clear annotations group (visual rendering)
                var el = this.element.getElementsByClassName('annotations')[0];
                if(el){
                    $(el).empty();
                }
                break;
        
            case 'headSelected':
                // Store head based on click position
                this.editor.arrow.head.x = event['offsetX'] / this.width;
                this.editor.arrow.head.y = event['offsetY'] / this.height;

                // Start the tail at the same location as the head (so it grows out from the head)
                this.editor.arrow.tail.x = event['offsetX'] / this.width;
                this.editor.arrow.tail.y = event['offsetY'] / this.height;
                
                this.renderArrow();
                break;

            case 'tailSelected':
                break;
        }
    };

    Plugin.prototype.renderArrow = function(){
        var el = this.element.getElementsByClassName('annotations')[0];
        if(el){
            // Draw svg arrow based on arrow.head and tail
            // Namedspaced functions are required for xml based svg (aka jquery wont work)
            this.editor.arrow.element = document.createElementNS('http://www.w3.org/2000/svg', 'line');
            
            this.editor.arrow.element.setAttributeNS(null, 'x1', this.width  * this.editor.arrow.head.x);
            this.editor.arrow.element.setAttributeNS(null, 'y1', this.height * this.editor.arrow.head.y);
            this.editor.arrow.element.setAttributeNS(null, 'x2', this.width  * this.editor.arrow.tail.x);
            this.editor.arrow.element.setAttributeNS(null, 'y2', this.height * this.editor.arrow.tail.y);
            this.editor.arrow.element.setAttributeNS(null, 'stroke', this.editor.arrow.color);
            this.editor.arrow.element.setAttributeNS(null, 'stroke-width', this.editor.arrow.size + 'px');
            this.editor.arrow.element.setAttributeNS(null, 'marker-start', 'url(#arrow)');

            // Append svg arrow to annotations group
            el.appendChild(this.editor.arrow.element);
        }
    };
    
    // Generic jquery plugin creation / destroy
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
