var playingAudio = false;

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

function notYetImplemented(){
    $.toast("Not yet implemented :(");
}

function goToView(view){
    $(".view").hide();
    $(view).show();
    //showToast("goToView: " + view);
}

function showToast(msg){
    //Uncomment this for additional on-screen debugging info during AA session
    //$.toast(msg);
}

function loadAlbums() {
    Android.loadAlbums("");
}
function loadWebradio() {
    Android.loadWebradio("");
}

function stopPlaying(){
    showToast("stopPlaying");
    $(".audio-player")[0].pause();
    this.currentTime = 0;
    playingAudio = false;
}

function playEvent(){
    //$.toast("playEvent");
    playPause();
}
function stopEvent(){
    //$.toast("stopEvent");
    playPause();
}
function pauseEvent(){
    //$.toast("pauseEvent");
    playPause();
}
function seekEvent(pos){
    //$.toast("seekEvent: " + pos);
}
function customEvent(act){
    //$.toast("customEvent: " + act);
}

function playPause(){
    showToast("playPause");
    if(playingAudio){
        $(".audio-player")[0].pause();
        playingAudio = false;
    } else {
        $(".audio-player")[0].play();
        playingAudio = true;
    }
}

function playPrevTrack(){
    showToast("playPrevTrack");
    var prevTrack = $(".track-item-selected").prev();
    if($(prevTrack).hasClass("track-item")){
        playTrack(prevTrack);
    } else {
        playTrack($(".track-item").last());
    }
}

function playNextTrack(){
    showToast("playNextTrack");
    var nextTrack = $(".track-item-selected").next();
    if($(nextTrack).hasClass("track-item")){
        playTrack(nextTrack);
    } else {
        playTrack($(".track-item").first());
    }
}
function playTrack(element){
    showToast("playTrack");
    $(".track-item").removeClass("track-item-selected");
    $(element).addClass("track-item-selected");
    var file = $(element).data("file");
    console.log(b64DecodeUnicode(file));
    $(".audio-player").children()[0].src = "file://" + b64DecodeUnicode(file);
    $(".audio-player")[0].load();
    $(".audio-player")[0].play();
    playingAudio = true;
    //show notification
    Android.showPlayNotification($(element).data("id"));

}


function playPauseWebradio(element){

    //is the current item playing?
    var currentPlaying = $(element).data("playing");

    //The currently playing item was clicked -> webradio should stop
    if(currentPlaying){
        //force an empty source to stop using bandwidth
        $(".audio-player").children()[0].src = "";
        $(".audio-player")[0].load();
        $(".audio-player")[0].play();
        $(element).data("playing", false);
        playingAudio = false;
        return;
    }

    //A different item has been clicked
    $(".radio-item").data("playing", false);
    var stream = $(element).data("stream");
    $(".audio-player").children()[0].src = stream;
    $(".audio-player")[0].load();
    $(".audio-player")[0].play();
    $(element).data("playing", true);
    playingAudio = true;

}

function createRadioGrid(webRadio){
    var webRadioObjects = JSON.parse(webRadio);
    showToast("Radio items: " + webRadioObjects.length);
    $(".radio-container").empty();
    $.each(webRadioObjects, function(i,e){
        var artistHTML = "<div class='artist-text-item'><span class='track-item-title'>"+e.title+"</span></div>";
        var albumHTML = "<div class='album-text-item'><span class='track-item-title'>"+e.subtitle+"</span></div>";
        var albumArtFile = e.art;
        if(e.art === undefined){
            albumArtFile = "img/nocover.png";
        }
        var coverHTML = "<div><img class='album-img' src='"+e.cover+"'></img></div>"
        var indexLetter = e.title.charAt(0).toUpperCase();
        $(".radio-container").append("<div onClick='playPauseWebradio(this);' data-playing='false' data-stream='" + e.stream + "' class='radio-item' data-index='"+indexLetter+"'>"+artistHTML+albumHTML+coverHTML+"</div>");
    });

}

function createTrackGrid(jsonTracks){
    var trackObjects = JSON.parse(jsonTracks);
    var coverArt = "";
    var fileExt = "";
    var year = "";
    var albumid = "";
    $(".track-container").empty();
    $.each(trackObjects, function(i,e){
        $(".track-container").append("<div onClick='playTrack(this);' class='track-item' data-id='"+e.id+"' data-file='"+b64EncodeUnicode(e.data)+"'><div><span class='track-item-title'>"+e.title+"</span></div></div>");
        coverArt = e.coverArt;
        if(coverArt === undefined){
            coverArt = "img/nocover.png";
        }
        fileExt = e.fileExt;
        year = e.year;
        albumid = e.albumId;
    });
    $(".file-type-box").text(fileExt);
    $(".year-box").text(year);
    if(year === undefined){
        $(".year-box").hide();
    } else {
        $(".year-box").show();
    }
    $(".coverart-img")[0].src = coverArt;
    $($(".coverart-img")[0]).data("albumid", albumid);

    goToView(".track-view");
    hideAlphaIndex();
    window.scrollTo(0, 0);

}

function loadTracks(id) {
    Android.loadTracks(id);
}

function createAlbumGrid(jsonAlbums){
    var albumObjects = JSON.parse(jsonAlbums);
    showToast("Album items: " + albumObjects.length);
    $.each(albumObjects, function(i,e){
        var artistHTML = "<div class='artist-text-item'><span class='track-item-title'>"+e.artist+"</span></div>";
        var albumHTML = "<div class='album-text-item'><span class='track-item-title'>"+e.name+"</span></div>";
        var albumArtFile = e.art;
        if(e.art === undefined){
            albumArtFile = "img/nocover.png";
        }
        var coverHTML = "<div><img class='album-img' src='"+albumArtFile+"'></img></div>";
        var indexLetter = e.artist.charAt(0).toUpperCase();
        $(".album-container").append("<div onClick=\"loadTracks('"+e.id+"');\" class='album-item' data-index='"+indexLetter+"' data-albumid='"+e.id+"'>"+artistHTML+albumHTML+coverHTML+"</div>");
    });

    goToView(".album-view");
    initAlphaIndex(".album-item");

}

function initAudioPlayer(){
    //bind event to detect when a track has finished playing
    var audioPlayer = $(".audio-player")[0];
    audioPlayer.addEventListener("ended", function(){
          audioPlayer.currentTime = 0;
          playNextTrack();
     });

}


function initView(){
    showToast("initView");
    loadAlbums();
    loadWebradio();
    initAudioPlayer();
    restoreSettings();
    applyTheme();
    setTimeout(function(){
        $("body").css("visibility", "visible");
    },150);

}