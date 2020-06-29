# Asus-ROG-Phone-2-RGB
This app allows you to control the logo rgb on custom roms. 
Works on stock rom too after release 2.0-hotfix

Note I have only tested it on android 10.

#### Only use this if you are using Asus Rog Phone 2
#### You need to be rooted to use this since it writes to system files
#### This modifies system files so use it at your own risk

# Sections
[Setup](#setup)

[Animations](#animations-currently-available)

[Usage](#usage)

[Screenshots](#screenshots)

[Issues](#issues)

[Features to be implemented](#features-to-be-implemented)

[Version](#version)

# Setup 
1. Download the latest apk from [releases](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/releases).
2. Find it in your downloads folder and click on it to install.
3. Grant the root request

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

# Screenshots
![screenshot 1](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/1.jpg)
![screenshot 2](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/2.jpg)
![screenshot 3](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/3.jpg)
![screenshot 4](https://github.com/ArtiomSu/Asus-ROG-Phone-2-RGB/blob/master/.screenshots/4.jpg)

# Issues
1. colour wheel performance. This is improved as of version 3 but could still be smoother 

# Features to be implemented
1. animation speed should be possible
2. blink delay should be possible too
3. maybe custom animations?
4. set animations for when receiving notifications/calls so on...

# Version
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
