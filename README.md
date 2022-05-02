# Asus-ROG-Phone-2-RGB
This app allows you to control the logo rgb led and the scond led rgb on custom rom and stock roms.

This app also has some extra features not found on the asus stock app ( See links bellow for each feature ). 

#### And yes the LEDs can light up during a phone call you just need to select your phone app in the "select which apps trigger notifications" setting, its as simple as that.

If you are using stock rom please disable any led effects in the built-in app as both apps will try to write different data at the same time causing unexpected results.

Works on Android 10,11,12 stock and custom roms. Stock android 9 rom doesn't work and will never work as the rgb driver doesn't react to changes on android 9.

#### Only use this if you are using Asus Rog Phone 2 or 3

#### You need to be rooted to use this since it writes to the led driver which is not possible to do without root access.

#### Use at your own risk although it should be pretty safe as it writes to the built in asus led driver.

[![](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=terminal_heat_sink.asusrogphone2rgb)

# Sections
[Setup](#setup)

[Animations](#animations-currently-available)

[Usage](#usage)

[Notifications](#notifications)

[Second led](#second-led)

[Notification Timeout](#notification-timeout)

[Per App Custom Animations and Colours](#per-app-custom-animations-and-colours)

[Notification Snooze](#notification-snooze)

[Save Restore App Data](#save-restore-app-data)

[Battery Animations](#battery-animations)

[Music Visualiser](#music-visualiser)

[Triple Shake](#triple-shake)

[Smart Home Device Controls](#smart-home-device-controls)

[Issues](#issues)

[Features](#features)

[Version](#version)

[Screenshots](#screenshots)

# Setup 
1. Download the latest apk from [releases](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases).
2. Find it in your downloads folder and click on it to install.
3. Grant the root request
4. Take a look at [Save Restore App Data](#save-restore-app-data) to restore old data.

### Video Guides
##### release 30 
[![release 30 video](https://img.youtube.com/vi/WONmNu35GFM/0.jpg)](https://www.youtube.com/watch?v=WONmNu35GFM)

##### release 9 
[![release 9 video](https://img.youtube.com/vi/8rNJtEDuV1I/0.jpg)](https://www.youtube.com/watch?v=8rNJtEDuV1I)


# Animations currently available 
0. off
1. solid one colour
2. breathing one colour
3. blink
4. rainbow
5. another rainbow?
6. rainbow breathe
7. somekind of thunder
8. thunder rainbow
9. quick two flashes
10. quick two flashes rainbow
11. breathe rainbow
12. some strange breathe rainbow
13. slow glitchy rainbow
14. yellow light? rofl

# Usage
To use it press the power button on the bottom right of the screen. ( see screen shots bellow ) 

When it turns green we are good to go and you can head over the animations tab.

The Colour wheel allows you to change the colour provided the animations aren't rainbow.

Colour wheel will work for the following animations.
1. solid one colour
2. breathing one colour
3. blink
4. some kind of thunder
5. quick two flashes
6. and sort of the last one too

# Notifications
Notification support is available from version 4 currently in beta and on the notifications branch.

To use the notifications 

1. You will find notification settings in the animations tab.
2. Press on React to Notifications to start the service.
3. Only needed before release 14 ~~Press on the "Click to enable app to read notifications in settings" button and allow Asus Rog Phone 2 RGB to read notifications~~
4. Select which animation you want to run when there is a notification
5. Press on "Select which apps trigger notifications" and tick on all the apps you want the app to use leds when a notification come in.
6. Optionaly you can select notification timeout see [Notification Timeout](#notification-timeout)
7. You can select different animations or colours for each app see [Per App Custom Animations and Colours](#per-app-custom-animations-and-colours)

### Second led
With the release of version 5 you can also enable the use of the second led that's normally ment to be used with a custom case.

The "enable second led" switch above "Notification settings" switch enables the second led for any animations selected above. This doesnt have any impact on using the second led for notifications.

For notifications you now have the following options relating to the second led.
1. "use second led for notifications also" This lights up the second led and the logo led when a notification comes in.
2. "Use Only the second led for notifications" This lights up only the second led.
3. If you have both 1 and 2 disabled then only the logo led with light up.

### How do the notifications work?
When a new notification comes in, the app reads the package name and saves it. The selected notification animation is executed.

When you remove the notification from the status bar the app also detects this and restores the animations to whatever you selected in the app, If you didnt select anything then it will just turn them off.

##### What if multiple notifications come in at once?
The app tracks the latest notification, and only checks if the latest is removed. So if you have a whats app notification followed by a telegram notification. The notification animation will keep playing until you remove the telegram notification.

This will allow setting custom notification animations for each app. ( in progress )

##### How do you deal with spammy notifications?
As of release 6 users can select which applications they want to trigger leds from an app list, illiminating the need for a blacklist now.

### Notification Timeout
This feature was added in release 7. It allows you to choose a time between 20 seconds and 9 hours, this will determine the maximum time it takes before the leds are turned off provided the notification is not cleared.

The slider ( or seekbar in android lingo ) is setup so that it increases exponentially meaning it is easier to dile in smalled values, For example half way through the bar is only 4 minutes. 3/4 of the way is around 1 hour and it will increase quickly to 9 hours and 33 minutes at the end.

###### Why is this feature neccessary?
If you leave the notification option on all the time, you can get a notification during the night for example and the leds will be blinking for the whole night, decreasing your battery by a bit.

However with notification timeout enabled you can set it so the leds stop after say 20 minutes of not clearing them.

###### How does it work?
A thread is created from the NotificationService class when the leds are ment to react to the notification. The thread checks if the leds have been flashing for specified amount of time and if the notification itself is not cleared. 

If the notification is cleared then it will call the same code that the NotificationService uses to stop and restore the leds.

If another notification comes in while there is already leds blinking for a notification then the timer is essentially reset and will wait to timeout for the new notification. This is because currently only the latest notification is being tracked. Future release might track every notification that comes in but this is pointless at the moment since the animations for every notification is the same.

### Per App Custom Animations and Colours
This feature was introduced after release 11. 
It allows each app to have it own unique animation and colours for the leds. So you can set all if your Telegram notifications to blink blue for example, all of say youtube notifications to breath red and so on.

###### To use this mode
1. In the notification settings click on "Select which apps trigger notifications"
2. Click on the app which you want to enable, it should turn green with a green asus rog logo.
3. To select which animation you want to use for the app click on the apps icon and a new view will come up saying "custom notification options for (app name)"
4. From here you can enable your desired animation for this app.
5. Also make sure the standard notification options are enabled as here [Notifications](notifications)

### Notification Snooze
This feature allows you to set a time window for when the notification service can be stopped. So if you dont want your leds to be blinking during the night then this feature is for you.

This feature is also recommended for rog 3 users as in order for rog 3 leds to work the app needs to hold a wakelock which consumes battery. So with notification snooze the wakelock is released thereby saving rog 3 users some battery.

###### How to Use Notification Snooze
1. Click on "Show notification snooze settings" to expand the menu.
2. Select "Set start time" this is when you want the notification service to stop. set a future time. so for example if right now its 20:45 set 20:46 since if you set 20:44 it will trigger tommorow.
3. Select "set end time" set this after your start time.
4. select "enable snooze" to start the thing.

###### Notification Snooze Best Practises
NOTE: if you change the start time or end time you will need to select "enable snooze" again it will be unselected anyway once you change the settings.

NOTE on Delay: cause of how repeating alarm manager works the times you set will not be accurate.
So for example if you set start time for 20:10 it will probably stop the notification service at like 20:15 maybe earlier or even later.

### Save Restore App Data
Since release 13 you can now save and restore your data, this way you dont have to setup all of the settings for custom notifications each time you reinstall the app.

###### How to
1. Change any setting in the app that you want and when finished go to the about tab.
2. Click on the "Export Settings" button to save.
3. After reinstalling the app or whenever. You can click on "Import Settings", the app will restart and your previous settings will be applied.

###### How does it work
Because this app uses root access we can take advantage of the shared preferences file.

When you save your settings, the app copies `/data/data/terminal_heat_sink.asusrogphone2rgb/shared_prefs/terminal_heat_sink.asusrogphone2rgb.xml` into `/sdcard/.terminal_heat_sink.asusrogphone2rgb.xml` 

When you restore the settings, the app copies the file back into the shared preferences folder and restarts the app so that it can read the new shared preferences therby restoring the apps settings to what they were.

### Battery Animations
This feature is available since release 15. When enabled a service will receive broadcasts about the battery status and will apply a colour based on the battery percentage. 0% = red, 100% = green, anything in between will be a smooth gradient achieved by adjusting the hue.


This feature takes notifications into account, so if there is a notification present then the battery service wont update the leds, as notification visualisation is more important then battery.


The service updates the leds about every 5 seconds, This is because the build in android system only broadcasts battery changes every 5 second on average.


However connecting and disconnecting from the charger is broadcast almost immediately so there is no issues there.

### Music Visualiser
Introduced in release 16. When enabled the leds dance to the music currently playing on the phone.

Notifications take priority over the visualiser. Once notifications are cleared visualiser will continue.

Removed in release 27. There are issues with it in android 11 and 12. Not alot of people are using it including myself to justify further development.

###### Modes
1. rgb, the hue is changed according to the waveform
2. glow, the lightness of the colour is changed according to the waveform, It will use the colour you selected in the colorwheel tab.

### Triple Shake
Triple Shake allows you to quickly shake your phone 3 times to toggle the second led when enabled.

The second led will use whatever mode you selected above at the very top of the App.

It will also use the colour you selected in the colour wheel for the appropriate modes.

I made this because I use the second led as a flashlight sometimes so this is a quick shortcut lol

removed in release 27 as not alot of people are using it. I no longer use it also since I use the case which illuminates with the second led.

### Smart Home Device Controls
Added in release 27. Allows you to use the smart home device controls to control various settings within the app ( see screenshots bellow ).

This is basicaly the android 11 power menu shortcuts. On Android 12 you can access these from the quick settings as it is no longer part of the the power menu.

This only works with android 11+ as the API was introduced in android 11.

# Issues
1. colour wheel performance. This is improved as of version 3 but could still be smoother.
2. app list can take a while to load because it gets the app icon for each application on the phone.

# Features
1. animation speed should be possible (might not be possible)
2. blink delay should be possible too (might not be possible)
3. maybe custom animations? (in progress)
4. set animations for when receiving notifications/calls so on... (done)
5. enable the use of second led (done)
6. allow timeout for notifications (done)
7. add quicktiles for toggling logo led (done), second led (done) and notifications ( in progress )
8. allow filtering when selecting apps (done)
9. set custom animations and colours for each app (done)
10. save restore app settings (done)
11. battery charging animations (done) 
12. music visualiser (done)(can be improved)(removed in v27)
13. Notification Snooze (done)
14. Triple shake (done)(removed in v27)
15. Smart Home Device Controls (done)

# Version
28. [6.1-tutorial-update](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/31) 
```
    Added a help section to the settings tab with the latest video guide. Can be hidden by pressing the button and shown again by pressing the button at the very bottom of the screen under the easter egg.
    Updated libraries and compile target
```
27. [6.0-home-device-controls-new-style](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/30) 
```
    Added Smart Home Device Controls. ( Android 11 power button shortcuts. Can still be used with Android 12 from the quick settings )
    Updated App Style. The different settings sections are now easily more distinguishable.
    Updated About Page to use a webview to show the github readme page and added link to my donation app.
    Removed Music Visualiser as it causes issues with Android 11+ and no one really uses it.
    Removed Triple Shake, again because no one really uses it.
```
26. [5.6-rog2-fallback](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/29) 
```
    Fallback to rog 2 mode if failed to determine which phone. ( can happen with android 12 gsi )
```
25. [5.5-tools-update](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/28) 
```
    Updated all build tools.
    Changed wrong phone and wrong os errors to warnings. ( Some people using GSI had wrong build props..... so this will be easier for them. )
```
25. [5.4-a11fix](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/27) 
```
    added permission for android 11 that allows to view all user apps.
```
25. [5.2-magisk](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/25) 
```
    New magisk mode added that can be triggered from the about tab and pressing the create magisk module button.

    This will generate a magisk module, uninstall the app and reboot your phone.
    The magisk module should allow all services to run without needing the notification as android hopefully wont kill a system app.

    To uninstall simply remove the magisk module from magisk manager.

    NOTE: This is completely optional the functionality is identical for the magisk version as the default.

    NOTE: This is a testing feature so there could be some issues. I will test it for about a week before uploading to the playstore if everything is ok
```
24. [5.1-fixes](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/24) 
```
    triple shake should be less sensitive now ( so it shouldn't trigger in your pocket )
    fixed triple shake being always active even when turning off the service
    improve drag/swipe inside colour picker. You still can't drag on the brightness bar unfortunately
```
23. [5.0-shake](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/23) 
```
    Added triple shake to miscellaneous settings.
    When enabled this allows you to shake your phone 3 times quickly to toggle the second led
```
22. [4.9-notification_snooze](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/22) 
```
    Added notification snooze feature which turns off the notification service during set times
    Show instructions for notification snooze on the first time you enable it
    Updated about
```
21. [4.8-notification_service](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/21) 
```
    Improved notification service. It now exists properly if not needed.
    Rog 3 notifications should now be fully working thanks to @jawadzoha from my telegram group for rog 3 testing.
```
20. [4.7-colour_crash_wakelock](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/20) 
```
    Fixed a crash when selecting colour
    Added rog 3 wakelock for rog 3 mode thanks again to @jawadzoha from my telegram group for rog 3 testing.
```
19. [4.6-colour_brightness_rog3_loop](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/19) 
```
    Added Brightness bar to colour wheel tab and when you select PerApp notification colours. 
    Also added a preview at the bottom of the colour picker so that you can get a more accurate idea of what it will look like.

    Rog 3 notifications update: 
    In rog 3 mode there is now a timer refreshing the driver every 30 seconds because rog 3 is a good phone.
    Thanks again to @jawadzoha from my telegram group for rog 3 testing.
```
18. [4.5-rog3_notifications](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/18) 
```
    notification support for rog3
    choice between rog 2 / rog 3 on first launch.
    app should launch on android 11 now
    magisk root notifications are turned off on first launch
```
17. [4.4-rog3](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/17) 
```
    Rog 3 support added.
    All functions work except being able to use only the second led
    Thanks to @jawadzoha from my telegram group for massive testing.

    fixed out of bounds bug that crashes app
```
16. [4.3-audio-visualiser](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/16) 
```
    added an audio visualiser. It has two different mode at the moment. rainbow and single colour mode that you can select from the colourwheel tab
```
15. [4.2-battery](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/15) 
```
    Added battery animations when charging.
    UI update to fix logos from touching each other
```
14. [4.1-settings-bypass](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/14) 
```
    Tiny update that removes the need for the "Click to enable App to read notifications in settings" button. This is because I have figured out how to grant this  permission using a command.

    The "React to Notifications" checkbox now enables and disables access accordingly.
```
13. [4.0-save-data](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/13) 
```
    Added two buttons in the about tab to save and restore all of the settings in the app.
    This way you wont have to set up all of the custom notifications for each app every time there is a new release.
```
12. [3.9-ui-update](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/12) 
```
    Changed all of the switches to use checkboxes, functionality is identical to previous release just visuals changed
```
11. [3.8-per-app-animations](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/11) 
```
    You can now set custom animations/colours for each app by clicking on the app icon in app select
```
10. [3.6-ui-detection-easter-eggs](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/10) 
```
    Added ability to type to filter applications in app select
```
9. [3.6-ui-detection-easter-eggs](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/9) 
```
    Allow more scrolling in all three tabs, this way on smaller screen settings you can 
    scroll down so the power buttons are not blocking the view.

    Implemented os and device checks. App will only run on asus and android 10.

    SystemWriter updated to read files for future.

    UI update

    Easter eggs - animate bg....
```
8. [3.5-quick-tile](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/8) 
```
    Added two quick tiles for toggling the logo led and second led.
```
7. [3.4-beta-delay](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/7) 
```
    New feature added that allows the lights to stop or timeout after a number of minutes or whatever you select.
    So this way if a notification comes on during the night the leds won't be shinning all night long.

    Update to UI
    AnimationsActivity heavily refactored.
    Timer Runnable created in NotificationService
```
6. [3.3-beta-app-select](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/6) 
```
    Added a view to select applications that you want to trigger the leds when they receive notifications.
    Improved UI a little bit
    Updated the about page
```
5. [3.2-beta-second_led](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/5) 
```
    This is a beta release that introduced the use of the second led
```
4. [3.1-beta-notifications](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/4) 
```
    This is a beta release that introduced notification support
```
3. [3.0-performance](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/3) 
```
    Rewrote SystemWriter to only write once per action thereby increasing performance mostly for colour wheel
    ColorWheel fragment now only writes to system if the data is changed instead of everytime the app opens or twice when colour is changed.
```
2. [2.0-hotfix](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/2)
```
    Issue with fragment not working on stock rom fixed. Stock rom tested working ok without any crashes
```
1. [initial_release](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases/tag/1)
```
    Working on HavocOS only.
```
# Screenshots
![screenshot 0](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/0.png)
![screenshot 1](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/1.png)
![screenshot 2](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/2.png)
![screenshot 4](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/4.png)
![screenshot 5](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/5.png)
![screenshot 6](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/6.png)
![screenshot 7](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/7.png)
![screenshot 8](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/8.png)
![screenshot 10](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/10.png)
![screenshot 11](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/11.jpg)
![screenshot 12](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/12.jpg)
![screenshot 13](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/13.jpg)
![screenshot 14](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/14.jpg)
![screenshot 15](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/15.jpg)
![screenshot 16](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/16.jpg)
![screenshot 17](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/17.png)
![screenshot 18](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/18.png)
![screenshot 19](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/19.png)
