   function EnhancedAudioControl(container, htmlAudioElement) {
       this.container = container;
       this.htmlAudioElement = htmlAudioElement;
       this.setEventHandlers();
       this.setHTMLAudioEvents();
       this.infotext = "";
   }
   EnhancedAudioControl.prototype.setEventHandlers = function() {
       var _this = this;
       $(this.container).find(".audioControlPlayPauseIcon").on("click", function() {
           _this.eacPlayPause(this);
       });
       $(this.container).find(".audioControlStopIcon").on("click", function() {
           _this.eacStop(this);
       });
       $(this.container).find(".audioControlSeek").on("input", function() {
           _this.eacOnSeek(this);
       });
       $(this.container).find(".audioControlPrevTrackIcon").on("click", function() {
           _this.eacPrevTrack(this);
       });
       $(this.container).find(".audioControlInfoIcon").on("click", function() {
           _this.eacShowInfo(this);
       });
       $(this.container).find(".audioControlNextTrackIcon").on("click", function() {
           _this.eacNextTrack(this);
       });
       $(this.container).find(".audioControlShuffleIcon").on("click", function() {
           _this.eacShuffleMode(this);
       });
       $(this.container).find(".audioControlRepeatIcon").on("click", function() {
           _this.eacRepeatMode(this);
       });
       $(this.container).find(".audioControlMuteIcon").on("click", function() {
           _this.eacMute(this);
       });
   };
   EnhancedAudioControl.prototype.setHTMLAudioEvents = function() {
       var _this = this;
       this.htmlAudioElement.ontimeupdate = function(d) {
           _this.eacSetSeekPosition();
       };
       this.htmlAudioElement.onended = function(d) {
           _this.eacSetSeekPosition();
           _this.eacStop();
       };
       this.htmlAudioElement.onpause = function(d) {};
       this.htmlAudioElement.onloadeddata = function(d) {
           _this.eacSetSeekPosition();
       };
   };
   EnhancedAudioControl.prototype.eacOnSeek = function() {
       this.htmlAudioElement.currentTime = $(".audioControlSeek").val();
   };
   EnhancedAudioControl.prototype.eacSetSeekPosition = function() {
       current = Math.round(this.htmlAudioElement.currentTime);
       duration = Math.round(this.htmlAudioElement.duration);
       $(".audioControlSeek").val(current);
       $(".audioControlSeek").attr("max", duration);
       //set time
       if (isNaN(duration) || duration === Infinity) {
           $(".audioControlTime").text(this.fmtMSS(current));
       } else {
           $(".audioControlTime").text(this.fmtMSS(current) + "/" + this.fmtMSS(duration));
       }
   };
   EnhancedAudioControl.prototype.eacPlay = function(uiElm) {
        this.htmlAudioElement.play();
        $(".audioControlPlayPauseIcon").text('\uF3E4');
   };
   EnhancedAudioControl.prototype.eacPlayPause = function(uiElm) {
       if (this.htmlAudioElement.paused) {
           this.htmlAudioElement.play();
           $(uiElm).text('\uF3E4');
       } else {
           this.htmlAudioElement.pause();
           $(uiElm).text('\uF40A');
       }
   };
   EnhancedAudioControl.prototype.eacShowInfo = function() {
       $.toast(this.infotext);
   };
   EnhancedAudioControl.prototype.eacPrevTrack = function() {
       $.toast("Not yet implemented :(");
   };
   EnhancedAudioControl.prototype.eacNextTrack = function() {
       $.toast("Not yet implemented :(");
   };
   EnhancedAudioControl.prototype.eacStop = function(uiElm) {
       this.htmlAudioElement.currentTime = 0;
       this.htmlAudioElement.pause();
       $(".audioControlPlayPauseIcon").text('\uF40A');
   };
   EnhancedAudioControl.prototype.eacShuffleMode = function(uiElm) {
       $(uiElm).text($(uiElm).text() === '\uF49D' ? '\uF49E' : '\uF49D');
   };
   EnhancedAudioControl.prototype.eacRepeatMode = function(uiElm) {
       $(uiElm).text($(uiElm).text() === '\uF456' ? '\uF457' : '\uF456');
   };
   EnhancedAudioControl.prototype.eacMute = function(uiElm) {
       $(uiElm).text($(uiElm).text() === '\uF581' ? '\uF57E' : '\uF581');
       this.htmlAudioElement.muted = !this.htmlAudioElement.muted;
   };
   EnhancedAudioControl.prototype.setSource = function(source) {
       this.htmlAudioElement.src = source;
   };
   EnhancedAudioControl.prototype.setSourceType = function(sourceType) {
       $(".audioControlMeta").text(sourceType);
   };
   EnhancedAudioControl.prototype.fmtMSS = function(s) {
       return (s - (s %= 60)) / 60 + (9 < s ? ':' : ':0') + s
   };