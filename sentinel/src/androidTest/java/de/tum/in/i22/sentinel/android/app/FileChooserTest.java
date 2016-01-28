package de.tum.in.i22.sentinel.android.app;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import de.tum.in.i22.sentinel.android.app.file_explorer.FileChooser;

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
public class FileChooserTest {

    @Rule
    public ActivityTestRule<FileChooser> mFC = new ActivityTestRule<FileChooser>(FileChooser.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, FileChooser.class);
            result.putExtra("extension", ".txt");
            return result;
        }
    };

    @BeforeClass
    public static void createFiles() {
        File f = new File("/sdcard/a");
        f.mkdirs();
        File fa = new File("/sdcard/a/ab.txt");
        File fb = new File("/sdcard/a/abc.jpg");
        try {
            fa.createNewFile();
            fb.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoesDirectoryAppears() throws Exception {
        onView(withText("a")).check(matches(isDisplayed()));
        onView(withText("a")).perform(click());
        onView(withText("ab.txt")).check(matches(isDisplayed()));
        onView(withText("abc.jpg")).perform(click());
        onView(withText("ab.txt")).perform(click());
    }

    @AfterClass
    public static void deleteCreatedFiles() {
        File f = new File("/sdcard/a");
        File fa = new File("/sdcard/a/ab.txt");
        File fb = new File("/sdcard/a/abc.jpg");
        fb.delete();
        fa.delete();
        f.delete();
    }
}
