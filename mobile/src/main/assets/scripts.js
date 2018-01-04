function b64EncodeUnicode(str) {
    return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function(match, p1) {
        return String.fromCharCode(parseInt(p1, 16))
    }))
}
function b64DecodeUnicode(str) {
    return decodeURIComponent(Array.prototype.map.call(atob(str), function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    }).join(''))
}

function goToAlbums(toast) {
    Android.goToAlbums("XX");
}
function playNextTrack(){
    var nextTrack = $(".track-item-selected").next();
    if($(nextTrack).hasClass("track-item")){
        playTrack(nextTrack);
    } else {
        playTrack($(".track-item").first());
    }
}
function playTrack(element){
    $(".track-item").removeClass("track-item-selected");
    $(element).addClass("track-item-selected");
    var file = $(element).data("file");
    console.log(b64DecodeUnicode(file));
    $("#audio-player").children()[0].src = "file://" + b64DecodeUnicode(file);
    $("#audio-player")[0].load();
    $("#audio-player")[0].play();
}
function createTrackGrid(jsonTracks){
    var obj = JSON.parse(jsonTracks);
    var coverArt = "";
    var fileExt = "";
    var year = "";
    $("#track-container").empty();
    $.each(obj, function(i,e){
        $("#track-container").append("<div onClick='playTrack(this);' class='track-item' data-file='"+b64EncodeUnicode(e.data)+"'><div><span class='track-item-title'>"+e.title+"</span></div></div>");
        coverArt = e.coverArt;
        if(coverArt === undefined){
            coverArt = "img/nocover.png";
        }
        fileExt = e.fileExt;
        year = e.year;
    });
    $(".file-type-box").text(fileExt);
    $(".year-box").text(year);
    if(year === undefined){
        $(".year-box").hide();
    } else {
        $(".year-box").show();
    }
    $("#coverart-img")[0].src = coverArt;
    $("#track-container").show();
    $("#cover-container").show();
    $("#audio-player").show();
    $("#album-container").hide();
}

function selectTrack(toast) {
    Android.selectTrack(toast);
}

function createAlbumGrid(jsonAlbums){
    var obj = JSON.parse(jsonAlbums);
    $("#album-container").empty();
    $.each(obj, function(i,e){
        var artistHTML = "<div class='artist-text-item'><span class='track-item-title'>"+e.artist+"</span></div>";
        var albumHTML = "<div class='album-text-item'><span class='track-item-title'>"+e.name+"</span></div>";
        var albumArtFile = e.art;
        if(e.art === undefined){
            albumArtFile = "img/nocover.png";
        }
        var coverHTML = "<div><img class='album-img' src='"+albumArtFile+"'></img></div>"
        $("#album-container").append("<div onClick=\"selectTrack('"+e.id+"');\" class='album-item'>"+artistHTML+albumHTML+coverHTML+"</div>");
    });
    $("#track-container").hide();
    $("#cover-container").hide();
    $("#audio-player").hide();
    $("#album-container").show();
}

function initView(){
    goToAlbums();

    //bind event to detect when a track has finished playing
    var audioPlayer = $("#audio-player")[0];
    audioPlayer.addEventListener("ended", function(){
          audioPlayer.currentTime = 0;
          playNextTrack();
     });
}