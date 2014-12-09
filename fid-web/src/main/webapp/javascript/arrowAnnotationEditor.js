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
        },
        
        style: {
            size: 5,
            color: '#ff0000',
        }
    };
    
    function Plugin(element, params){
        // Check for required elements withing the svg
        this.annotations = element.getElementsByClassName('annotations')[0];
        if(!this.annotations){
            return;
        }

        this.pluginParams = $.extend({}, defaults, params);
        
        this.index = 0;
        this.state = ['empty', 'headSelected', 'tailSelected'];

        this.arrow = this.pluginParams.arrow;
        this.arrow.element = null;
        
        this.style = this.pluginParams.style;
        
        this.element = element;
        this.width   = $(element).width();
        this.height  = $(element).height();

        // Check for existing arrow values
        if( this.arrow.head.x !== null &&
            this.arrow.head.y !== null &&
            this.arrow.tail.x !== null &&
            this.arrow.tail.y !== null ){
            
            // Update editor state
            this.index = this.state.length -1;
            this.renderArrow();
        }
        
        // Set arrow head color
        var poly = this.element.getElementsByClassName('arrow-poly');
        for(var i = 0, len = poly.length; i < len; i++ ){
            poly[i].setAttributeNS(null, 'fill', this.style.color);
        }
        
        $(element).on('mousemove', $.proxy(this.move,   this));
        $(element).on('mousedown', $.proxy(this.click, this));
    };

    Plugin.prototype.updateArrowOnServer = function(){
        var url = new String(this.apiUrl) +
            '&x='+  this.arrow.head.x +
            '&y='+  this.arrow.head.y +
            '&x2='+ this.arrow.tail.x +
            '&y2='+ this.arrow.tail.y;
        wicketAjaxGet(url, function(){}, function(){});
    }    

    // Prepare values for storing in the DB
    Plugin.prototype.calculateX = function(value){
        return value / this.width;
    };
    Plugin.prototype.calculateY = function(value){
        return value / this.height;
    };
    
    // Track mouse move after the head point has been selected
    Plugin.prototype.move = function(event){
        if( this.state[ this.index] === 'headSelected' ){
            // Store tail based on current mouse position
            this.arrow.tail.x = this.calculateX( event['offsetX'] );
            this.arrow.tail.y = this.calculateY( event['offsetY'] );

            if( this.arrow.element !== null ){
                this.arrowAttribute('x2', event['offsetX']);
                this.arrowAttribute('y2', event['offsetY']);
            }
        }
    };
    
    // State machine for tracking click events
    Plugin.prototype.click = function(event){

        // Update editor state
        this.index = ++this.index % this.state.length;

        switch( this.state[this.index] ){
                
            case 'empty':
                // Clear internal data structure
                this.arrow.head.x = null;
                this.arrow.head.y = null;
                this.arrow.tail.x = null;
                this.arrow.tail.y = null;

                // Clear annotations group (visual rendering)
                $(this.annotations).empty();
                break;
        
            case 'headSelected':
                // Store head based on click position
                this.arrow.head.x = this.calculateX( event['offsetX'] );
                this.arrow.head.y = this.calculateY( event['offsetY'] );
                
                // Start the tail at the same location as the head (so it grows out from the head)
                this.arrow.tail.x = this.calculateX( event['offsetX'] );
                this.arrow.tail.y = this.calculateY( event['offsetY'] );
                
                this.renderArrow();
                break;

            case 'tailSelected':
                this.updateArrowOnServer();
                break;
        }
    };
    
    // Helper to set attributesNS on the arrow element
    Plugin.prototype.arrowAttribute = function(key, value){
        this.arrow.element.setAttributeNS(null, key, value);
    };
    
    Plugin.prototype.renderArrow = function(){
        // Draw svg arrow based on arrow.head and tail
        // Namedspaced functions are required for xml based svg (aka jquery wont work)
        this.arrow.element = document.createElementNS('http://www.w3.org/2000/svg', 'line');
        
        this.arrowAttribute('x1', this.width  * this.arrow.head.x);
        this.arrowAttribute('y1', this.height * this.arrow.head.y);
        this.arrowAttribute('x2', this.width  * this.arrow.tail.x);
        this.arrowAttribute('y2', this.height * this.arrow.tail.y);
        this.arrowAttribute('stroke',       this.style.color);
        this.arrowAttribute('stroke-width', this.style.size + 'px');
        this.arrowAttribute('marker-start', 'url(#arrow)');

        // Append svg arrow to annotations group
        this.annotations.appendChild(this.arrow.element);
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
