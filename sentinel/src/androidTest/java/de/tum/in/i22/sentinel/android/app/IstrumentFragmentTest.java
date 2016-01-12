package de.tum.in.i22.sentinel.android.app;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import de.tum.in.i22.sentinel.android.app.test_resources.ElapsedTimeIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Moderbord on 2016-01-12.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IstrumentFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mFC = new ActivityTestRule<MainActivity>(MainActivity.class){
    };

    @BeforeClass
    public static void createFolder(){
        File a = new File("/sdcard/a");
        File b = new File("/sdcard/b");
        File c = new File("/sdcard/c");
        a.mkdirs();
        b.mkdirs();
        c.mkdirs();
        File d = new File("/sdcard/a/ModerMath.apk");
        File aa = new File("/sdcard/a/aaa.txt");
        File bb = new File("/sdcard/b/bbb.txt");
        File cc = new File("/sdcard/c/ccc.txt");
        try {
            aa.createNewFile();
            bb.createNewFile();
            cc.createNewFile();
            d.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoesDirectoryAppears() throws Exception {
        onView(withId(R.id.drawer_layout)).perform(actionOpenDrawer());
        IdlingResource x = new ElapsedTimeIdlingResource(200);
        Espresso.registerIdlingResources(x);
        onView(withText("Instrument")).perform(click());

        onView(withId(R.id.sinksButton)).perform(click());
        onView(withText("a")).perform(click());
        onView(withText("aaa.txt")).perform(click());

        onView(withId(R.id.sourcesButton)).perform(click());
        onView(withText("b")).perform(click());
        onView(withText("bbb.txt")).perform(click());

        onView(withId(R.id.taintButton)).perform(click());
        onView(withText("c")).perform(click());
        onView(withText("ccc.txt")).perform(click());

        onView(withId(R.id.applicationButton)).perform(click());
        IdlingResource z = new ElapsedTimeIdlingResource(3000);
        Espresso.registerIdlingResources(z);
        onView(withId(R.id.pickButton)).perform(click());
        onView(withText("a")).perform(click());
        onView(withText("ModerMath.apk")).perform(click());

        onView(withId(R.id.clearButton)).perform(scrollTo());
        Espresso.registerIdlingResources(x);
        onView(withId(R.id.clearButton)).perform(click());

        onView(withId(R.id.nextActivityButton)).perform(click());

    }

    @AfterClass
    public static void deleteFiles(){
        File a = new File("/sdcard/a");
        File b = new File("/sdcard/b");
        File c = new File("/sdcard/c");
        File d = new File("/sdcard/a/ModerMath.apk");
        File aa = new File("/sdcard/a/aaa.txt");
        File bb = new File("/sdcard/b/bbb.txt");
        File cc = new File("/sdcard/c/ccc.txt");
        d.delete();
        aa.delete();
        bb.delete();
        cc.delete();
        a.delete();
        b.delete();
        c.delete();
    }


    private static ViewAction actionOpenDrawer() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "open drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(GravityCompat.START);
            }
        };
    }

    /*private static ViewAction actionCloseDrawer() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "close drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).closeDrawer(GravityCompat.START);
            }
        };
    }*/

}
