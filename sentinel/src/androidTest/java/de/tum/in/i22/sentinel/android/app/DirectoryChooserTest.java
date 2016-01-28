package de.tum.in.i22.sentinel.android.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.file_explorer.DirectoryChooser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Moderbord on 2016-01-12.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DirectoryChooserTest {

    @Rule
    public ActivityTestRule<DirectoryChooser> mFC = new ActivityTestRule<DirectoryChooser>(DirectoryChooser.class) {
    };

    @BeforeClass
    public static void createFolder() {
        File f = new File("/sdcard/Downloads/a");
        File fb = new File("/sdcard/Downloads/ab");
        f.mkdirs();
        fb.mkdirs();
    }

    @Test
    public void testDoesDirectoryAppears() throws Exception {
        onView(withText("Download")).check(matches(isDisplayed()));
        onView(withText("Download")).perform(click());
        onView(withText("..")).check(matches(isDisplayed()));
        onView(withText("..")).perform(click());
        onView(withText("DCIM")).check(matches(isDisplayed()));
        onView(withText("DCIM")).perform(click());
        onView(withText("Choose this directory")).check(matches(isDisplayed()));
        onView(withText("Choose this directory")).perform(click());
    }

    @AfterClass
    public static void deleteFolder() {
        File f = new File("/sdcard/Downloads/a");
        File fb = new File("/sdcard/Downloads/ab");
        f.delete();
        fb.delete();
    }

}
