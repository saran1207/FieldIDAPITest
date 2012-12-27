function putSpinnersOverImages() {
    $('.rich-text img').each(function(index, img) {
        var realSource = img.src;
        var realWidth = img.width;

        img.src = "/fieldid/images/loader.gif";
        img.width = 16;

        var imageLoader = new Image();
        imageLoader.src = realSource;
        if (imageLoader.complete) {
            img.src = realSource;
            img.width = realWidth;
        } else {
            imageLoader.onload = function() {
                img.src = realSource;
                img.width = realWidth;
                imageLoader.onload=function(){};
            };
        }
    });
}