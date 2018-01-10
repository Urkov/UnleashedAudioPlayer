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

function iconActionTop() {
    $('html, body').animate({ scrollTop: 0 }, 200);
}

function iconActionAlpha() {
    $('.a-selector-index').fadeToggle();
}

function iconActionBottom() {
    $('html, body').animate({ scrollTop: $(document).height() }, 200);
}

//initialize
$(".album-item").each(function(i, e) {
    var idx = $(e).data("index");
    if (!indexExists(idx)) {
        $(".a-selector-index").append("<div data-index='" + idx + "'><span>" + idx + "</span></div>");
    }
});

//bind js function
$(".a-selector-index div").each(function(i, e) {
    $(e).click(function() {
        goToByScroll($(this).data("index"), ".album-item");
        $('.a-selector-index').fadeToggle();
    });
});