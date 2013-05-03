
function setupPrintPage(options)
{
   // don't add page break to first header
   $(".header").first().css("page-break-before", "avoid");

    var $header = $('.header');


//    var isolationPointSize = 45;  //  or 75 or 110 depending on setting.   use options.spacing to get this value.
    var isolationPointSize = parseInt(options.printOption);
    var pagePixelCount = 0;


    var $ul = $('<ul></ul>');
    $ul.appendTo($('#print-images-container'));
    $('#print-images li').each(function(index,value) {
        if (index%3==0) {
            pagePixelCount =  pagePixelCount + 250;

        }
        if (pagePixelCount>815) {
            $header.clone().appendTo($ul);
            pagePixelCount = 0;
            $ul = $('<ul></ul>');
            $ul.appendTo($('#print-images-container'));
        }
        $(value).clone().appendTo($ul);
    });

    $('#print-images').hide();    // delete this element after.


    //pagePixelCount will be 0..900
    //create UL.   append to "current UL".   when > 900 pixels. close UL and open next current UL.

   // $ul = $('<ul></ul>').addClass('isolation-point-table');
   // $ul.appendTo($('#print-isolation-points-list'));
    //$('.isolation-point-table li').each(function(index,value) {


    var $ulI = $('<ul class="isolation-point-table"></ul>');;
    $ulI.appendTo($('#print-isolation-points-list'));
    pagePixelCount = 0;

    var $heading = $('#iso-table-headings').clone();


    $('#iso-table li').each(function(index,value) {

      pagePixelCount = pagePixelCount + isolationPointSize;

      if (pagePixelCount>815) {
            $header.clone().insertAfter($ulI);
            pagePixelCount = 0;
            $ulI = $('<ul class="isolation-point-table"></ul>');
            $heading.clone().appendTo($ulI);
            $ulI.appendTo($('#print-isolation-points-list'));
      }

        $(value).appendTo($ulI);
    });

   // $('.isolation-point-table').hide();  // remove this stuff, delete element.
   $('#iso-table').hide();


}
