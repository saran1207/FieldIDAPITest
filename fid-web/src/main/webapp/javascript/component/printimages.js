
function setupPrintPage(options)
{
   // don't add page break to first header
  // $(".header").first().css("page-break-before", "avoid");
    var $header = $('.header');

    //hide watermark if not PUBLISHED
    var state = options.state;

    if (options.state == 'PUBLISHED') {
       $('.watermark').hide();
    }

    var $page = $('<div class="printpage" id="printpage">');
    $page.appendTo($('#print-images-container'));

    var $wmark = $('.watermark').clone();

//  var isolationPointSize = 45;  //  or 75 or 110 depending on setting.   use options.spacing to get this value.
    var isolationPointSize = parseInt(options.printOption);
    var pagePixelCount = 0;

    var $ul = $('<ul></ul>');
   // $ul.appendTo($('#print-images-container'));
    $ul.appendTo($('.printpage'));
  //  $wmark.appendTo($('.printpage'));


    $('#print-images li').each(function(index,value) {
        if (index%3==0) {
            pagePixelCount =  pagePixelCount + 250;

        }
        if (pagePixelCount>815) {
           // $header.clone().appendTo($ul);

            $page = $('<div class="printpage" id="printpage">');
            $page.appendTo($('#print-images-container'));
            var $hclone = $header.clone()
            $hclone.insertAfter($ul);

            pagePixelCount = 0;
            $ul = $('<ul></ul>');
            //$ul.appendTo($('#print-images-container'));

            $ul.appendTo($page);
            $wmark.clone().appendTo($hclone);

//        } else {
//
//            var $hclone = $header.clone()
//            $hclone.insertAfter($ul);
//            $wmark.clone().appendTo($hclone);
        }


        $(value).clone().appendTo($ul);
    });
     $('#print-images').remove();    // delete this element after.



//  var isolationPointSize = 45;  //  or 75 or 110 depending on setting.   use options.spacing to get this value.
    var $pagei = $('<div class="printpage" id="printpage">');
    $pagei.appendTo($('#print-isolation-points-list'));

    var $ulI = $('<ul class="isolation-point-table"></ul>');
    $ulI.appendTo($pagei);
    pagePixelCount = 0;

    var $heading = $('#iso-table-headings').clone();

    $('#iso-table li').each(function(index,value) {

      pagePixelCount = pagePixelCount + isolationPointSize;

        if (pagePixelCount>815) {
            $pagei = $('<div class="printpage" id="printpage">');
            $pagei.appendTo($('#print-isolation-points-list'));

            var $cheader = $header.clone();
            $cheader.insertAfter($ulI);

            pagePixelCount = 0;
            $ulI = $('<ul class="isolation-point-table"></ul>');
            $heading.clone().appendTo($ulI);
            $ulI.appendTo($pagei);
            $wmark.clone().appendTo($cheader);

        }
       $(value).appendTo($ulI);
    });

    $('#iso-table').hide();

}