# Android Auto Music Player App based on HTML5 for hassle-free track-choose

## Warning:

DO NOT USE WHILE DRIVING! KEEP YOUR EYES ON THE ROAD! BAD THINGS CAN HAPPEN!
I am not responsible for any damage caused by this application.

## Screenshots

![alt text](https://raw.githubusercontent.com/nerone-github/UnleashedAudioPlayer/master/images/ua2.png)
![alt text](https://raw.githubusercontent.com/nerone-github/UnleashedAudioPlayer/master/images/ua3.png)
![alt text](https://raw.githubusercontent.com/nerone-github/UnleashedAudioPlayer/master/images/ua4.png)

## Dependencies:

- https://github.com/martoreto/aauto-sdk (The piece of code which made OEM apps for AA possible in the first place - Good Job!)

## Features:

- It can play music ;-)
- Various color themes
- Supports media buttons of steering wheel (might not work in all cars - please create issue if not working) 

## How to install:

- Build and install APK (If you want to skip the build, [download APK here](https://github.com/nerone-github/UnleashedAudioPlayer/raw/master/apk/unleashedaudio.apk)).
- Enable Developer settings in Android Auto
    ![alt text](https://raw.githubusercontent.com/nerone-github/LocalSpeedcam/master/images/devsettings.png)
- Enable 'Unknown sources' checkbox
- Connect phone to AA Head Unit and select the OEM tab
- App permissions must be granted manually via the App menu of the phone, or by launching the phone Activity first

## How to use:

- Select album in the main view
- Select a track in the track view
- To return to album view, tap on the art cover

## Webradio support

- Create "Radio" folder at the root of your internal storage (External sd card won't work)
- Copy a radio.txt file which contains the radio stations and their covers. An example follows
- To start playing tap on the cover. Tap again to stop playing.
- Caution: Webradio might consume a lot of data. Check your tariff before using.

```
[
   {
      "title":"Radio Station 1",
      "subtitle":"320kbit",
      "stream":"http://url.mp3",
      "cover":"data:image/png;base64,????????????"
   },
   {
      "title":"Radio Station 2",
      "subtitle":"128kbit",
      "stream":"http://url.mp3",
      "cover":"data:image/png;base64,????????????"
   }
]
```


