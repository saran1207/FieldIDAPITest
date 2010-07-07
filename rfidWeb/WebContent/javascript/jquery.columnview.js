/**
 * jquery.columnview-1.2.js
 *
 * Created by Chris Yates on 2009-02-26.
 * http://christianyates.com
 * Copyright 2009 Christian Yates and ASU Mars Space Flight Facility. All rights reserved.
 *
 * Supported under jQuery 1.2.x or later
 * Keyboard navigation supported under 1.3.x or later
 * 
 * Dual licensed under MIT and GPL.
 */

(function($){
  $.fn.columnview = function(options){
    
    var settings = $.extend({}, $.fn.columnview.defaults, options);
        
    // Add stylesheet, but only once
    if(!$('.containerobj').get(0)){
      $('head').prepend('\
      <style type="text/css" media="screen">\
        .containerobj {\
          border: 1px solid #ccc;\
          background-color:#FFFFFF;\
          height:15em;\
          overflow-x:auto;\
          overflow-y:hidden;\
          white-space:nowrap;\
          position:relative;\
        }\
        .containerobj div {\
          height:100%;\
          overflow-y:scroll;\
          overflow-x:hidden;\
          position:absolute;\
          max-width:200px;\
        }\
        .containerobj a {\
          display:block;\
          clear:both;\
          white-space:nowrap;\
          min-width:185px;\
          padding-right:15px;\
        }\
        .containerobj a:focus {\
          outline:none;\
        }\
        .containerobj a canvas{\
          padding-left:1em;\
        }\
        .containerobj .feature {\
          min-width:200px;\
        }\
        .containerobj .feature a {\
          white-space:normal;\
        }\
        .containerobj .hasChildMenu {\
        }\
        .containerobj .active {\
          background-color:#3671cf;\
          color:#fff;\
        }\
        .containerobj .inpath {\
          background-color:#d0d0d0;\
          color:#000;\
        }\
        .containerobj .hasChildMenu .widget{\
          color:black;\
          position:absolute;\
          right:0;\
          text-decoration:none;\
          font-size:0.7em;\
        }\
        .treeHeading{\
        	background-color:#D0DAFA;\
        	color:#333333;\
        	font-weight:bold;\
           }\
        </style>');
    }

    // Hide original list
    $(this).hide();
    // Reset the original list's id
    var origid = $(this).attr('id');
    $(this).attr('id', origid + "-processed");

    // Create new top container from top-level LI tags
    var top = $(this).children('li');
    var container = $('<div/>').addClass('containerobj').attr('id', origid).insertAfter(this);
    var topdiv = $('<div class="top"></div>').appendTo(container);
    if($.browser.msie) { $('.top').width('200px'); } // Cuz IE don't support auto width
    $.each(top,function(i,item){
      var topitem = $(':eq(0)',item).clone().data('sub',$(item).children('ul')).appendTo(topdiv);
      if($(topitem).data('sub').length) {
        $(topitem).addClass('hasChildMenu');
        addWidget(topitem);
      }
    });

    // Event handling functions
    $(container).bind("click keydown", function(event) {
      if ($(event.target).is("a") || $(event.target).parent('a')) {
        var self = event.target;
        if (!settings.multi) {
          delete event.shiftKey;
          delete event.metaKey;
        }
        self.focus();
        var container = $(self).parents('.containerobj');
        // Handle clicks
        if (event.type == "click"){
          var level = $('div',container).index($(self).parents('div'));
          
          
          
          // Remove blocks to the right in the tree, and 'deactivate' other
          // links within the same level, if metakey is not being used
          $('div:gt('+level+')',container).remove();
          if (!event.metaKey && !event.shiftKey) {
            $('div:eq('+level+') a',container).removeClass('active').removeClass('inpath');
            $('.active',container).addClass('inpath');
            $('div:lt('+level+') a',container).removeClass('active');
          }
          // Select intermediate items when shift clicking
          // Sorry, only works with jQuery 1.4 due to changes in the .index() function
          if (event.shiftKey) {
            var first = $('a.active:first', $(self).parent()).index();
            var cur = $(self).index();
            var range = [first,cur].sort(function(a,b){return a - b;});
            $('div:eq('+level+') a', container).slice(range[0], range[1]).addClass('active');
          }
          $(self).addClass('active');
          if ($(self).data('sub').children('li').length  && !event.metaKey) {
            // Menu has children, so add another submenu
        	     submenu(container,self);
          }
          
          
          //Scroll to the right whenever a new node is selected.
          $(container).scrollLeft("100000");
          
        }
        // Handle Keyboard navigation
        if(event.type == "keydown"){
          switch(event.keyCode){
            case(37): //left
              $(self).parent().prev().children('.inpath').focus().trigger("click");
              break;
            case(38): //up
              $(self).prev().focus().trigger("click");
              break;
            case(39): //right
              if($(self).hasClass('hasChildMenu')){
                $(self).parent().next().children('a:first').focus().trigger("click");
              }
              break;
            case(40): //down
              $(self).next().focus().trigger("click");
              break;
            case(13): //enter
              $(self).trigger("dblclick");
              break;
          }
        }
        event.preventDefault();
      }
    });

    
    
    $(self).selectNode(settings['defaultSelection'], origid);
    
  }

  
  $.fn.columnview.defaults = {
    multi: false,
    defaultSelection: -1
  };

  	
  // Generate deeper level menus
  function submenu(container,item){
    var leftPos = 0;
    $.each($(container).children('div'),function(i,mydiv){
    //Currently, the width() function is borked in msie. Hardcode width for the 
    //time being. 
    //leftPos += $(mydiv).width();
    	leftPos += 200;
    });
    
    var submenu = $('<div/>').css({'top':0, 'left':leftPos}).appendTo(container);
    if($.browser.msie) { $(submenu).width('200px'); } // Cuz IE don't support auto width
    var subitems = $(item).data('sub').children('li');
    $.each(subitems,function(i,subitem){
      var subsubitem = $(':eq(0)',subitem).clone().data('sub',$(subitem).children('ul')).appendTo(submenu);
      if($(subsubitem).data('sub').length) {
        $(subsubitem).addClass('hasChildMenu');
        addWidget(subsubitem);
      }
    });
  }

  // Uses canvas, if available, to draw a triangle to denote that item is a parent
  function addWidget(item, color){
    var triheight = $(item).height();
    var canvas = $("<canvas></canvas>").attr({height:triheight,width:10}).addClass('widget').appendTo(item);    if(!color){ color = $(canvas).css('color'); }
    canvas = $(canvas).get(0);
    if (canvas.getContext){
      var context = canvas.getContext('2d');
      context.fillStyle = color;
      context.beginPath();
      context.moveTo(3,(triheight/2 - 3));
      context.lineTo(10,(triheight/2));
      context.lineTo(3,(triheight/2 + 3));
      context.fill();
    } else {
      /**
       * Canvas not supported - put something in there anyway that can be
       * suppressed later if desired. We're using a decimal character here
       * representing a "black right-pointing pointer" in Windows since IE
       * is the likely case that doesn't support canvas.
       */
      $("<span>&#9658;</span>").addClass('widget').css({'height':triheight,'width':10}).prependTo(item);
    }
    $('.widget').bind('click', function(event){
      event.preventDefault();
    });
  }

  
  
  $.fn.selectNode = function(selectedNodeId, origid) {
	  	var defaultNode = $('#' + origid + "-processed a[nodeId='" + selectedNodeId + "']");
	  	
  		
  		if (defaultNode.length == 0) {
  			selectedNodeId = -1;
  		} else {
  			var preSelectedValue = defaultNode.first();
			var targets = new Array();
			
			preSelectedValue.parents('li.expanded').each(function() {
				var nodeId = $(this).children('a[nodeId]').attr('nodeId');
				targets.push('#' + origid + " a[nodeId='" + nodeId + "']")
			});
			
			while(targets.length > 0) {
				$(targets.pop()).click();
			}
  		}
  		
		$('#' + origid + " a[nodeId='" + selectedNodeId + "']").click();
  };
})(jQuery);

