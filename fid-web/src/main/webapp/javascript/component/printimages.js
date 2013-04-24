
function setupPrintPage(options)
{

    var $header = $('.print-header');
    var count = $('div.print-images ul li').length;

    $('.print-images ul li').each(function(index,value) {

       if ((count > 1) && (index > 0) && (((index+1)%9)==0)) {
            $header.clone().appendTo(value);
        } else if ((count == 1) && (index == 0)) {
            $header.clone().appendTo(value);
        }  else {}

     });
}
