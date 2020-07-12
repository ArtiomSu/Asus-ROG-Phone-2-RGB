# Asus-ROG-Phone-2-RGB
This app allows you to control the logo rgb on custom roms. 
Works on stock rom too after release 2.0-hotfix

Only works on android 10 roms as the android 9 rgb driver doesn't react to changes, therefore it is probably not possible to make it work on android 9 using current methods. 

#### Only use this if you are using Asus Rog Phone 2
#### You need to be rooted to use this since it writes to system files
#### This modifies system files so use it at your own risk

# Sections
[Setup](#setup)

[Animations](#animations-currently-available)

[Usage](#usage)

[Notifications](#notifications)

[Second led](#second-led)

[Notification Timeout](#notification-timeout)

[Per App Custom Animations and Colours](#per-app-custom-animations-and-colours)

[Issues](#issues)

[Features](#features)

[Version](#version)

[Screenshots](#screenshots)

# Setup 
1. Download the latest apk from [releases](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases).
2. Find it in your downloads folder and click on it to install.
3. Grant the root request

If you want a video guide here is a youtube video showing all the features and how to use them for release 9 [click to view](https://www.youtube.com/watch?v=8rNJtEDuV1I)

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
3. Press on the "Click to enable app to read notifications in settings" button and allow Asus Rog Phone 2 RGB to read notifications
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

# Issues
1. colour wheel performance. This is improved as of version 3 but could still be smoother.
2. app list can take a while to load because it gets the app icon for each application on the phone.
3. notification service keeps on restarting when killed.

# Features
1. animation speed should be possible (might not be possible)
2. blink delay should be possible too (might not be possible)
3. maybe custom animations? (in progress)
4. set animations for when receiving notifications/calls so on... (done)
5. enable the use of second led (done)
6. allow timeout for notifications (done)
7. add quicktiles for toggling logo led (done), second led (done) and notifications ( in progress )
8. allow filtering when selecting apps (done)
9. set custom animations and colours for each app

# Version
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
![screenshot 1](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/1.jpg)
![screenshot 2](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/2.jpg)
![screenshot 3](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/3.jpg)
![screenshot 4](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/4.jpg)
![screenshot 5](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/5.jpg)
![screenshot 6](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/6.jpg)
![screenshot 7](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/7.jpg)
![screenshot 8](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/8.jpg)
![screenshot 9](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/9.jpg)
![screenshot 10](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/10.jpg)
![screenshot 11](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/11.jpg)
![screenshot 12](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/12.jpg)
![screenshot 13](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/13.jpg)
![screenshot 14](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/14.jpg)
![screenshot 15](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/15.jpg)
