function goToByScroll(idx, itemClass) {
    try {
        var el = $(itemClass + "[data-index=" + idx + "]").get(0);
        if (el !== undefined) {
            $('html,body').animate({
                scrollTop: $(el).offset().top
            }, 200);
        }
    } catch (e) {
        console.log(e);
    }
}

function indexExists(idx) {
    return $(".a-selector-index div[data-index=" + idx + "]").length > 0;
}

function iconActionScroll(amount){
    window.scrollBy(0,amount);
}

function iconActionTop() {
    $('html, body').animate({ scrollTop: 0 }, 400);
}

function iconActionAlpha() {
    $('.a-selector-index').fadeToggle();
}

function iconActionBottom() {
    $('html, body').animate({ scrollTop: $(document).height() }, 400);
}

function hideAlphaIndex(){
    $(".a-selector").hide();
    $(".a-selector-index").hide();
}

function initAlphaIndex(itemClass){
    showToast("initAlphaIndex");
    $(".a-selector-index").empty();
    //initialize
    $(itemClass).each(function(i, e) {
        console.log(e);
        var idx = $(e).data("index");
        if (!indexExists(idx)) {
            $(".a-selector-index").append("<div data-index='" + idx + "'><span>" + idx + "</span></div>");
        }
    });

    //bind js function
    $(".a-selector-index div").each(function(i, e) {
        $(e).click(function() {
            goToByScroll($(this).data("index"), itemClass);
            $('.a-selector-index').fadeToggle();
        });
    });
    //show the selector
    $(".a-selector").show();
}

