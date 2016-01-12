package de.tum.in.i22.sentinel.android.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.file_explorer.DirectoryChooser;

import static android.support.test.espresso.Espresso.onView;
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
    public ActivityTestRule<DirectoryChooser> mActivityRule = new ActivityTestRule<DirectoryChooser>(DirectoryChooser.class);

    @Before
    public void createFolder(){
        File f = new File("/sdcard/a");
        f.mkdirs();
    }

    @Test
    public void testDoesDirectoryAppears() throws Exception {
        onView(withText("a")).check(matches(isDisplayed()));
    }

    @After
    public void deleteFolder(){
        File f = new File("/sdcard/a");
        f.delete();
    }
}
