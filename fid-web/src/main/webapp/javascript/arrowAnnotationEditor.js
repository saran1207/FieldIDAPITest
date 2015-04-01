/*
 *-----------------------------------------------------------------------------
 * Copyright (c) 2011-2014 FieldID - All Right Reserved
 * Author: David Addison
 *
 * This file can not be copied and/or distributed without the express
 * permission of FieldID
 *-----------------------------------------------------------------------------
 */
;(function($, window, document, undefined){
    "use strict";

    var plugin = 'arrowAnnotationEditor';
    var defaults = {
        apiUrl: '/api?', // MANDATORY! - Url for AJAX

        // arrow data that will be move to/from the server via ajax
        // value ranges from 0 - 1.0
        arrow: {
            head: {'x':null, 'y':null},
            tail: {'x':null, 'y':null}
        },
        
        style: {
            line: {
                size: 5,
                color: '#ff0000',
            },   
            head: {
                offset: 5,   // offset from end
                length: 22,  // length of the arrow head
                indent: 18,  // length from front to back for indent
                width:  12,  // width of arrow head
                color: '#ff0000'
            }
        }
    };
    
    function Plugin(element, params){
        // Check for required elements within the svg
        this.annotations = $(element).find('.annotations').get(0);
        this.image       = $(element).find('image').get(0);
        if( (!this.annotations) || (!this.image) ){
            return;
        }
        
        this.pluginParams = $.extend(true, {}, defaults, params);
        
        // Initialize mouse state machine
        this.index = 0;
        this.state = ['empty', 'headSelected', 'tailSelected'];

        // Store reference of the data / style (more readable code)
        this.arrow = this.pluginParams.arrow;
        this.style = this.pluginParams.style;
        
        // Store reference to the calling element
        this.element = element;
        
        // Get the display height of the svg element
        this.displayHeight = $(element).height();

        // hardcoded height because reloading of the editor
        // is causing the image to reduce insize
        // this.displayHeight = 230; // same as css
        
        // Get the non-scaled image size
        this.imageWidth    = this.image.getAttributeNS(null, 'width');
        this.imageHeight   = this.image.getAttributeNS(null, 'height');
        this.imageWidth    = parseInt(this.imageWidth);
        this.imageHeight   = parseInt(this.imageHeight);

        // Calculate the scaling from displayed size to actual size
        this.scaling = this.imageHeight / this.displayHeight;
        
        // Scale the stroke and arrow head based on the scaling value
        // This will make the arrow appear uniform in the editor view
        // regardless of the size of underlying image
        this.style.line.size   *= this.scaling;
        this.style.head.offset *= this.scaling;
        this.style.head.length *= this.scaling;
        this.style.head.indent *= this.scaling;
        this.style.head.width  *= this.scaling;

        // Set the width of the parent element
        // Needed to correct tracking issue with IE when image is scaled
        $(element).parent().css('width', (this.imageWidth / this.scaling));

        // After:
        //  - getting the display height
        //  - getting the scaling value
        //  - setting the parent's width
        // Set the display width
        this.displayWidth = $(element).width();
        
        // After:
        //  - setting the paren't width
        //  - letting the margin re-adjust
        // Store the element's offset
        this.elementOffset = $(element).offset();
        
        // Check for arrow values being passed in as params
        if( this.arrow.head.x !== null &&
            this.arrow.head.y !== null &&
            this.arrow.tail.x !== null &&
            this.arrow.tail.y !== null ){
            
            // Update editor state to empty
            this.index = this.state.length -1;
            this.renderArrow();
        }

        $(element).bind('mousemove', $.proxy(this.move,  this));
        $(element).bind('mousedown', $.proxy(this.click, this));
    };

    Plugin.prototype.updateArrowOnServer = function(){
        // send coords as percentage values to the server
        var url = new String(this.pluginParams.apiUrl) +
            '&x='+  this.arrow.head.x +
            '&y='+  this.arrow.head.y +
            '&x2='+ this.arrow.tail.x +
            '&y2='+ this.arrow.tail.y;
        wicketAjaxGet(url, function(){}, function(){});
    }    

    // Calculate cross-browser mouse positions
    Plugin.prototype.position = function(event){
        // Offset is updated here because the element may
        // move if it is -for example- a modal dialog
        this.elementOffset = $(this.element).offset();

        var coords = {}
        // convert from screen to normalized values of 0 - 1.0
        coords.x = (event.pageX - this.elementOffset.left) / this.displayWidth;
        coords.y = (event.pageY - this.elementOffset.top)  / this.displayHeight;
        return coords;
    };
    
    // Track mouse move after the head point has been selected
    Plugin.prototype.move = function(event){
        if( this.state[ this.index] === 'headSelected' ){
            var position = this.position(event);
            
            // Store tail based on current mouse position
            this.arrow.tail.x = position.x;
            this.arrow.tail.y = position.y;
            
            this.renderArrow();
        }
    };
    
    // State machine for tracking click events
    Plugin.prototype.click = function(event){

        // Update editor state
        this.index = ++this.index % this.state.length;

        switch( this.state[this.index] ){
                
            case 'empty':
                // Clear stored arrow data
                this.arrow.head.x = null;
                this.arrow.head.y = null;
                this.arrow.tail.x = null;
                this.arrow.tail.y = null;
                
                // Clear annotations group (visual rendering)
                $(this.annotations).empty();
                break;
        
            case 'headSelected':
                var position = this.position(event);
                
                // Store head based on click position
                this.arrow.head.x = position.x;
                this.arrow.head.y = position.y;
                
                // Start the tail at the same location as the head (so it grows out from the head)
                this.arrow.tail.x = position.x;
                this.arrow.tail.y = position.y;
                
                this.renderArrow();
                break;

            case 'tailSelected':
                this.updateArrowOnServer();
                this.renderArrow();
                break;
        }
    };
    Plugin.prototype.renderArrow = function(){
        // Clear annotations group and remake
        $(this.annotations).empty();
        
        //------------------------------[ Update arrow geometry ]------
        // change stored values into screen coords
        
        // Get delta values (aka reference from 0,0)
        var deltaX = this.arrow.tail.x - this.arrow.head.x;
        var deltaY = this.arrow.tail.y - this.arrow.head.y;
        
        // Convert delta values from normalized to image coords (internal svg)
        deltaX = deltaX * this.imageWidth;
        deltaY = deltaY * this.imageHeight;
        
        // Get the length of the line
        var magnitude = Math.sqrt( (deltaX * deltaX) + (deltaY * deltaY) );
        
        // Calculate the unit vector
        var unitX = deltaX / magnitude;
        var unitY = deltaY / magnitude;

        //-------------------------------[ Perpendicular Vector ]------
        // Generate offset cp of the arrow (point down the line)
        var cpOffsetX = (this.arrow.head.x * this.imageWidth)  - (unitX * this.style.head.offset);
        var cpOffsetY = (this.arrow.head.y * this.imageHeight) - (unitY * this.style.head.offset);
        
        // Generate back cp of the arrow
        var cpBackX = cpOffsetX + (unitX * this.style.head.length);
        var cpBackY = cpOffsetY + (unitY * this.style.head.length);
        
        // Generate slight back indent for the arrow
        var cpIndentX = cpOffsetX + (unitX * this.style.head.indent);
        var cpIndentY = cpOffsetY + (unitY * this.style.head.indent);
        
        // Generate side cp of the arrow (left/right of the line)
        // cpBack is used to position the arrow head
        //     - at the head (arrow.x1)
        //     - move that position by head.length
        //     - add that position to the left/right positions
        // 
        // Basic form of perpendicular vector
        // left.x =  unit.y
        // left.y = -unit.x
        // right.x = -unit.y
        // right.x =  unit.x
        
        var cpLeftX = cpBackX + ( unitY * this.style.head.width);
        var cpLeftY = cpBackY + (-unitX * this.style.head.width);
        
        var cpRightX = cpBackX + (-unitY * this.style.head.width);
        var cpRightY = cpBackY + ( unitX * this.style.head.width);
        
        // Compile point data into something that svg polygon can use
        var points = '' +
            cpOffsetX +','+ cpOffsetY +' '+ 
            cpLeftX   +','+ cpLeftY   +' '+ 
            cpIndentX +','+ cpIndentY +' '+ 
            cpRightX  +','+ cpRightY;

        // Catch rare data error where the input numbers cause NaN
        if( points === 'NaN,NaN NaN,NaN NaN,NaN NaN,NaN' ){
            return;
        }
        
        //---------------------------------[ Render Arrow ]------------
        // Draw svg arrow based on arrow head and tail (data.x1,y1 data.x2,y2)
        // Namedspaced functions are required for xml based svg (aka jquery wont work)
        var line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
        line.setAttributeNS(null, 'x1', this.arrow.head.x * this.imageWidth);
        line.setAttributeNS(null, 'y1', this.arrow.head.y * this.imageHeight);
        line.setAttributeNS(null, 'x2', this.arrow.tail.x * this.imageWidth);
        line.setAttributeNS(null, 'y2', this.arrow.tail.y * this.imageHeight);
        line.setAttributeNS(null, 'stroke',       this.style.line.color);
        line.setAttributeNS(null, 'stroke-width', this.style.line.size + 'px');
        this.annotations.appendChild(line);
    
        // arrow head
        var polygon = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
        polygon.setAttributeNS(null, 'points', points);
        polygon.setAttributeNS(null, 'fill', this.style.head.color);
        this.annotations.appendChild(polygon);
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


// For backwards compatibility. This adds the upload button
// that was used in the original imageGallery panel
//function initUploadButton(){
//    var $originalSubmit = $('.arrow-style-annotation-panel .add-image');
//    var $indicator= $('<img class="indicator" src="/fieldid/images/loader.gif"/>');
//    if ($.browser.msie) {
//        // for IE use the ugly default. gives Access Denied if i try to delegate from prettier button.
//        $originalSubmit.change(function() {
//            $originalSubmit.attr('disabled',true);
//            $indicator.addClass('ie').insertAfter($originalSubmit);
//        })
//        return;;
//    } else {
//        // create a prettier submit button and delegate to the existing/underlying one.
//        if ($originalSubmit.length==0) {
//            throw "can't find ADD button. should have class '.image-gallery .add-image'";
//        }
//        $originalSubmit.hide();
//        var $newSubmit = $('<a>').html('Upload New Image').addClass('add-image').addClass('mattButton');
//        $originalSubmit.change(function() {
//            $indicator.insertAfter($newSubmit.parent());
//            $originalSubmit.attr('disabled',true);
//        });
//        $newSubmit.insertAfter($originalSubmit);
//        $newSubmit.click(function() {
//            $originalSubmit.click();
//        });
//    }
//};
//initUploadButton();