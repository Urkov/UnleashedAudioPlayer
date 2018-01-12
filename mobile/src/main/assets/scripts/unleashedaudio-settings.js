var settings = {};
settings.theme = 'default';
var themes = ['default', 'red1', 'green1', 'black1'];

function applyTheme(){
    $("#dynamic-stylesheet").attr("href", "css/themes/" + settings.theme + ".css");
}
function nextTheme(){
    var currentIndex = themes.indexOf(settings.theme);
    var nextIndex = currentIndex + 1 === themes.length ? 0 : currentIndex + 1;
    settings.theme = themes[nextIndex];
    //$.toast("Next theme: " + settings.theme);

}
function saveSettings(){
    //$.toast("Save settings: " + JSON.stringify(settings));
    localStorage.setItem("settings", JSON.stringify(settings));
}
function restoreSettings(){
    var savedSettings = JSON.parse(localStorage.getItem("settings"));
    if(savedSettings === null){
        saveSettings();
    }
    settings = JSON.parse(localStorage.getItem("settings"));
    //$.toast("Restore settings: " + JSON.stringify(settings));
}
