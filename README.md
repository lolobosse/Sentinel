# Sentinel app

## How to build it?

This is a gradle project, so you can build from Android Studio or directly from the command line.

###To build it in Android Studio:
**File > Open ... > Click on the build.gradle**
However, because you need to launch many apps in order that the project works, we strongly recommend to use the gradle console.

###To build from Gradle console
 * Open a terminal
 * Navigate to the project root
 * Execute `./gradlew installDebug` or `./gradlew.bat installDebug` (be careful with the file permission, give it the exectution permission `chmod +x`)
 * Ensure that a device is connected before doing so

##Requirements:
 * Internet Connection to download dependencies
 * Android device from 4.0 to 4.4 

## Test it
 * To test the app with the Android UnitTest we wrote, just run `./gradlew connectedCheck`
 * Coverage report is there `sentinel/build/reports/coverage/debug`