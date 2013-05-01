
function setupPrintPage(options)
{
   // don't add page break to first header
   $(".header").first().css("page-break-before", "avoid");

    var $header = $('.print-header');
    var count = $('div.print-images ul li').length;

//    var isolationPointSize = 45;  //  or 75 or 110 depending on setting.   use options.spacing to get this value.
    var isolationPointSize = options.printOption;
    var pagePixelCount = 0;


    var $ul = $('<ul></ul>');
    $ul.appendTo($('div.image-list'));
    $('.print-images ul li').each(function(index,value) {
        if (index%3==0) pagePixelCount += 250;
        if (pagePixelCount>815) {
            $header.clone().appendTo($ul);
            pagePixelCount = 0;
            $ul = $('<ul></ul>');
            $ul.appendTo($('div.image-list'));
        }
        $(value).clone().appendTo($ul);
    });

    $('.print-images').hide();    // delete this element after.

    //pagePixelCount will be 0..900
    //create UL.   append to "current UL".   when > 900 pixels. close UL and open next current UL.
    $ul = $('<ul></ul>').addClass('isolation-point-table');
    $ul.appendTo($('div.list'));
    $('.isolation-point-table li').each(function(index,value) {
        pagePixelCount += isolationPointSize;
        if (pagePixelCount>815) {
            //current UL = $('ul class='paginated-isolation-points');
            $header.clone().insertAfter($ul);
            pagePixelCount = 0;
            $ul = $('<ul></ul>');
            $ul.appendTo($('div.list'));
        }
        $(value).appendTo($ul);
    });

    $('.isolation-point-table').hide();   // remove this stuff, delete element.

};
