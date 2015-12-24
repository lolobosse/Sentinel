#Sentinel

##What is this?

Wouldn't be great to have an app managing all your instrumented app? Sentinel allows that!

Here are the great features:

 * Explore the app installed on your phone and see which one are instrumented and which not.
 * Run PDP as a Service
 * Edit policy easily and save them where you want
 * Deploy policy to PDP
 * Download already instrument apps from our Store!
 * Send your third party apk(s) to be secured by our systems and get an instrumented apk back.
 * Define your custom sinks and sources to be instrumented

## Roadmap

### What does already exist?

 * The command to instrument is known and the server is running properly
 * The policy can be saved to a file and retrieved from there
 * The app is working with independent fragments
 * Tests are running >70% coverage
 * We know how to send APK to the server, did tests in other apps.

### What do we need to do?

 * The UI : @Moderbord could do it yet if @sosaavedra merge my PR, if not @Moderbord can work from my repo
 * The Send/Receive/Install of APK: I did already so I just have to include it again, should not be a problem as soon as Sebastian fixed the wrong output.
 * Coverage to 100% which could be tricky with the server transfers.

## How to run the project?

 * @sosaavedra did a great work porting it to Studio, so import the whole repo to Studio
 * Click launch and wait and see
 * If it fails, install the build tools we use. We use Build Tool 22 for one VERY good reason that few know: if you run 23 without having implemented the new 23's permission system, you'll crash with a `Security Exception`
 * Enjoy!

### How to run tests?

 * `chmod +x` the `gradlew`
 * run `./gradlew connectedDebugAndroidTest` for UNIX and Windows, nah, I don't care!
 * To see the coverage report (if it worked), open that in browser: `/whereYouClonedIt/DroidForce/sentinel/build/reports/coverage/debug/index.html`