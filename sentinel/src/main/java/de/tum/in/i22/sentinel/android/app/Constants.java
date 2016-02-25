package de.tum.in.i22.sentinel.android.app;

import android.content.Context;

/**
 * Created by Moderbord on 2016-01-22.
 */

public class Constants {

    // Used by InstrumentFragment, ToServerFragment, StatusFragment
    public static final String BITMAP       = "bitmap";
    public static final String PACKAGE_NAME = "pn";
    public static final String LOGO         = "logo";
    public static final String APP_NAME     = "appname";
    public static final String INSTRUMENTED_APPLICATIONS = "instrumentedApplications";
    public static final String ABSOLUTE_PATH  = "GetAbsolutePath";

        // File extensions
    public static final String EXTENSION      = "extension";
    public static final String INPUT_TXT      = ".txt";
    public static final String INPUT_XML      = ".xml";
    public static final String INPUT_APK      = ".apk";

        // For SharedPreferences
    public static final String SENTINEL         = "sentinel";
    public static final String SP_PATH_APP      = "pathApp";
    public static final String SP_PATH_SINKS    = "pathSinks";
    public static final String SP_PATH_SOURCES  = "pathSources";
    public static final String SP_PATH_TAINT    = "pathTaint";
        // Used in Settings
    public static final String SP_SAVE_APK          = "saveAPK";
    public static final String SP_SAVE_APK_FOLDER   = "saveFolderAPK";

        // Used by ToServerFragment when retrieving paths to selected files
    public static final String APK      = "apkPath";
    public static final String SOURCES  = "sourcePath";
    public static final String SINKS    = "sinkPath";
    public static final String TAINT    = "taintPath";

    // Used by SettingsFragment
    public static final String COLOR_DARK = "#202020";
    public static final String COLOR_GREY = "#c5c5c5";

    // Used by PlayStoreFragment, PlayStoreDetail
    public static final String DETAILS_TO_DISPLAY_KEY = "DetailsOf";


    // Used by FileChooser, DirectoryChooser, MenuObj, SettingsFragment,
    public static final String PARENT_DIRECTORY = "Parent directory";
    public static final String DIRECTORY_PATH   = "directory_path";
    public static final String DIRECTORY_PICK   = "directory_pick";
    public static final String DIRECTORY_ICON   = "directory_icon";
    public static final String DIRECTORY_UP     = "directory_up";
    public static final String FILE_ICON        = "file_icon";

    // Used by APKReceiver
    public static final String APK_TYPE         = "application/vnd.android.package-archive";

    // Every key for the server (multipart and so on)

    public static String getServerAddress(Context c){
        return Utils.getServerAddress(c);
    }

    public static final String SERVER_APK_FILE                          = "apkFile";
    public static final String SERVER_SOURCE_FILE                       = "sourceFile";
    public static final String SERVER_SINK_FILE                         = "sinkFile";
    public static final String SERVER_TAINT_WRAPPER                     = "easyTaintWrapperSource";
    public static final String SERVER_LOGO_FILE                         = "logo";
    public static final String SERVER_APP_NAME                          = "apkName";
    public static final String SERVER_PACKAGE_NAME                      = "packageName";
    public static final String SERVER_PUBLIC_FLAG                       = "makeAppPublic";
    public static final String SERVER_INSTRUMENTATION_ENDPOINT          = "/instrument";
    public static final String SERVER_INSTRUMENTATION_ENDPOINT_META     = "/instrument/withmetadata";
    public static final String SERVER_INSTRUMENTATION_ENDPOINT_NO_META  = "/instrument/withoutmetadata";

    // HashCode of server signature
    public static final int SERVER_SIGNATURE_HASH_CODE = -1288784872;

    // Used by PolicyEditor
    public static final String AXEL_PACKAGE_NAME = "fr.xgouchet.xmleditor";
}
