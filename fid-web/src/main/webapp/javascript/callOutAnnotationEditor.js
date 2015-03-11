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

    var plugin = 'callOutAnnotationEditor';
    var defaults = {
        apiUrl: '/api?', // MANDATORY! - Url for AJAX
        
        style: {
            line: {
                length: 24,
                width:  5,
                offset: 10    // offset from the tip of the arrow
            },   
            head: {
                scale:  0.85, // overall scaling of the three params
                length: 22,   // length of the arrow head
                indent: 18,   // length from front to back for indent
                width:  12    // width of arrow head
            },
            label: {
                font:    20,  // font-size
                offsetY: 6    // offset from the top (baseline shift)
            },
            bg: {
                padding: 14,
                radius: 5,
                strokeWidth: 2
            }
        },

        //------------------------
        activeAnnotation: {
            x: null,
            y: null,
            label:  null,
            fill:   null,
            stroke: null
        },
        annotations: [
            // {
            //     x: 0.1,
            //     y: 0.1,
            //     label:  'N01-1234567890-1234567890-1234567890',
            //     fill:   '#FFFFFF',
            //     stroke: '#000000'
            // }
        ]
        //---------------------
    };
    
    function Plugin(element, params){
        // Check for required elements within the svg
        this.image = $(element).find('image').get(0);
        if( !this.image ){
            return;
        }

        // merge defaults with passed in params
        this.pluginParams = $.extend(true, {}, defaults, params);
        
        // store a reference to calling element
        this.element = element;
        
        // store a reference to style
        this.style = this.pluginParams.style;
        
        // Get the display height of the svg element
        this.displayHeight = $(element).height();
        
        // Get the non-scaled image size
        this.imageWidth  = this.image.getAttributeNS(null, 'width');
        this.imageHeight = this.image.getAttributeNS(null, 'height');
        this.imageWidth  = parseInt(this.imageWidth,  10);
        this.imageHeight = parseInt(this.imageHeight, 10);
        
        // Calculate the scaling from displayed size to actual size
        var scaling = this.imageHeight / this.displayHeight;
        
        // Scale the stroke and arrow head based on the scaling value
        // This will make the arrow appear uniform in the editor view
        // regardless of the size of underlying image
        this.style.line.width  *= scaling;
        this.style.line.length *= scaling;
        this.style.line.offset *= scaling;
        
        this.style.head.length *= scaling * this.style.head.scale;
        this.style.head.indent *= scaling * this.style.head.scale;
        this.style.head.width  *= scaling * this.style.head.scale;
        
        this.style.label.font    *= scaling;
        this.style.label.offsetY *= scaling;
        
        this.style.bg.padding     *= scaling;
        this.style.bg.radius      *= scaling;
        this.style.bg.strokeWidth *= scaling;
        
        // Set the width of the parent element (.svg-container)
        // Needed to correct tracking issue with IE when image is scaled
        $(element).parent().css('width', (this.imageWidth / scaling));
        
        // After:
        //  - getting the display height
        //  - getting the scaling value
        //  - setting the parent's width
        // Set the display width
        this.displayWidth = $(element).width();
        
        // store a reference help with readability
        this.activeAnnotation = this.pluginParams.activeAnnotation;
        this.annotations      = this.pluginParams.annotations;
        
        // Create container for other renderable annotations
        // contents will not be re-rendered or altered
        this.annotationGroup = document.createElementNS('http://www.w3.org/2000/svg', 'g');
        this.element.appendChild(this.annotationGroup);
        
        // Render other annotations
        for(var i = 0, len = this.annotations.length; i < len; i++){
            this.annotations[i].group  = this.annotationGroup;
            this.annotations[i].bbox   = this.getBounds(this.annotations[i]);
            
            this.annotations[i].width  = this.annotations[i].bbox.width
            this.annotations[i].width += this.style.bg.padding;
            this.annotations[i].width += this.style.line.length;
            
            this.renderNote(this.annotations[i]);
        }

        
        // Create container for editable annotation
        // contents will be destroyed and recreated during the editing process
        this.activeAnnotation.group = document.createElementNS('http://www.w3.org/2000/svg', 'g');
        this.element.appendChild(this.activeAnnotation.group);

        // Get label bounds and calculate the annotation width
        this.activeAnnotation.bbox   = this.getBounds(this.activeAnnotation);
        
        this.activeAnnotation.width  = this.activeAnnotation.bbox.width;
        this.activeAnnotation.width += this.style.line.length
        this.activeAnnotation.width += this.style.bg.padding;
        
        // Check for existing note values
        if( this.activeAnnotation.x < 0 &&
            this.activeAnnotation.y < 0 ){
            this.renderNote(this.activeAnnotation);
        }
        
        // Bind to click events
        $(element).bind('mousedown', $.proxy(this.click, this));
    };

    Plugin.prototype.getBounds = function(note){
        // Render then remove a copy of the label to calculate it's bounds
        var group = document.createElementNS('http://www.w3.org/2000/svg', 'g');
        var elem  = this.element.appendChild(group);
        
        var label = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        var labelNode = document.createTextNode(note.label);
        label.setAttributeNS(null, 'x', 0 );
        label.setAttributeNS(null, 'y', 0 );
        label.setAttributeNS(null, 'font-size', this.pluginParams.style.label.font +'px' );
        label.appendChild(labelNode);
        group.appendChild(label);
        var bbox = label.getBBox();
        $(elem).remove();
        
        return bbox;
    };
    
    Plugin.prototype.updateNoteOnServer = function(){
        var url = new String(this.pluginParams.apiUrl) +
            '&x='+  this.activeAnnotation.x +
            '&y='+  this.activeAnnotation.y;
        
        wicketAjaxGet(url, function(){}, function(){});
    };

    // Handle click events
    Plugin.prototype.click = function(event){
        // Store the element's offset
        // This has to happen after init for the following:
        //  - setting the parent's width
        //  - letting the margin re-adjust
        // Offset is stored here because the element may
        // move if it is -for example- a modal dialog
        this.elementOffset = $(this.element).offset();

        // convert from screen to normalized values of 0 - 1.0
        // Store new note position based on click position
        this.activeAnnotation.x = (event.pageX - this.elementOffset.left) / this.displayWidth;
        this.activeAnnotation.y = (event.pageY - this.elementOffset.top)  / this.displayHeight;

        // Clear annotations group and remake
        $(this.activeAnnotation.group).empty();
        
        this.renderNote(this.activeAnnotation);
        this.updateNoteOnServer();
    };
    
    Plugin.prototype.pointsString = function(data){
        var point_string = '';
        for( var i = 0, l = data.length; i < l; i++){
            point_string += ' ' + data[i].join(',');
        }
        return point_string;
    };
    
    Plugin.prototype.renderNote = function(note){
        
        //---------------------------------------------------------
        // Overview:
        //  - Create arrow head
        //  - Create arrow line
        //  - Create label rect from bounding information
        //  - Create label text
        //---------------------------------------------------------

        
        // switch to svg coordinates for rendering
        var np = {}
        np.x = (note.x * this.imageWidth);
        np.y = (note.y * this.imageHeight);
        
        // create a working copy of style
        var style = $.extend(true, {}, this.style);
        var bbox  = $.extend(true, {}, note.bbox);
        
        // set direction
        var direction = (note.x > 0.5) ? -1 : 1;
        style.head.length *= direction;
        style.head.width  *= direction;
        style.head.indent *= direction;
        style.line.offset *= direction;
        style.line.length *= direction;

        // Scale annotation to fit inside the image
        var fitWidth = (note.x > 0.5) ? np.x : (this.imageWidth - np.x);
        var fitScale  = fitWidth / note.width;

        if( fitScale < 1.0 ){
            style.line.width  *= fitScale;
            style.line.length *= fitScale;
            style.line.offset *= fitScale;

            style.head.length *= fitScale;
            style.head.indent *= fitScale;
            style.head.width  *= fitScale;

            style.label.font    *= fitScale;
            style.label.offsetY *= fitScale;

            style.bg.padding     *= fitScale;
            style.bg.radius      *= fitScale;
            style.bg.strokeWidth *= fitScale;
            
            bbox.width  *= fitScale;
            bbox.height *= fitScale;
        }
       
        // Set label offset based on direction
        var labelOffset = (note.x > 0.5) ? -(bbox.width + style.bg.padding) : 0;

        
        
        var arrowHead = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
        var points = [ 
            [ np.x, np.y ],
            [ (np.x + style.head.length), (np.y + style.head.width) ],
            [ (np.x + style.head.indent),  np.y ],
            [ (np.x + style.head.length), (np.y - style.head.width) ]
        ];
        arrowHead.setAttributeNS(null, 'points', this.pointsString(points));
        arrowHead.setAttributeNS(null, 'fill', note.stroke );
        note.group.appendChild(arrowHead);
        
        var arrowLine = document.createElementNS('http://www.w3.org/2000/svg', 'line');
        arrowLine.setAttributeNS(null, 'x1', np.x + style.line.offset);
        arrowLine.setAttributeNS(null, 'y1', np.y);
        arrowLine.setAttributeNS(null, 'x2', np.x + style.line.length);
        arrowLine.setAttributeNS(null, 'y2', np.y);
        arrowLine.setAttributeNS(null, 'stroke', note.stroke );
        arrowLine.setAttributeNS(null, 'stroke-width', style.line.width + 'px' );
        note.group.appendChild(arrowLine);
        
        var bg = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
        bg.setAttributeNS(null, 'x', np.x + labelOffset + style.line.length);
        bg.setAttributeNS(null, 'y', np.y - ((bbox.height + style.bg.padding) / 2));
        bg.setAttributeNS(null, 'width',  bbox.width  + style.bg.padding);
        bg.setAttributeNS(null, 'height', bbox.height + style.bg.padding);
        bg.setAttributeNS(null, 'rx', style.bg.radius );
        bg.setAttributeNS(null, 'ry', style.bg.radius );
        bg.setAttributeNS(null, 'fill', note.fill );
        bg.setAttributeNS(null, 'stroke', note.stroke );
        bg.setAttributeNS(null, 'stroke-width', style.bg.strokeWidth );
        note.group.appendChild(bg);
        
        var label = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        var labelNode = document.createTextNode(note.label);
        label.setAttributeNS(null, 'x', np.x + labelOffset + style.line.length + (style.bg.padding/2) );
        label.setAttributeNS(null, 'y', np.y + style.label.offsetY );
        label.setAttributeNS(null, 'font-size', style.label.font +'px' );
        label.setAttributeNS(null, 'fill', note.stroke );
        label.appendChild(labelNode);
        note.group.appendChild(label);
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
