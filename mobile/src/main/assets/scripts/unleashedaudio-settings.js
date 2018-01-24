var settings = {};
settings.backgroundcol = 'black';
settings.iconcol = '#03a9f4';
settings.textcol = 'white';

function applyTheme(){
    document.body.style.setProperty("--backgroundcol", settings.backgroundcol);
    document.body.style.setProperty("--iconcol", settings.iconcol);
    document.body.style.setProperty("--textcol", settings.textcol);
}

function saveSettings(){
    //$.toast("Save settings: " + JSON.stringify(settings));
    localStorage.setItem("csssettings", JSON.stringify(settings));
}
function restoreSettings(){
    var savedSettings = JSON.parse(localStorage.getItem("csssettings"));
    if(savedSettings === null){
        saveSettings();
    }
    settings = JSON.parse(localStorage.getItem("csssettings"));
    //$.toast("Restore settings: " + JSON.stringify(settings));
}
