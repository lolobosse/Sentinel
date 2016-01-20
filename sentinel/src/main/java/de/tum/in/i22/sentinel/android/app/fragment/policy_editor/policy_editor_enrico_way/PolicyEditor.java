package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.policy_editor_enrico_way;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import de.tum.in.i22.sentinel.android.app.R;

/**
 * Created by laurentmeyer on 19/01/16.
 */
public class PolicyEditor extends Fragment {

    FragmentPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.enrico_policy_editor_layout, container, false);
        adapter = new CustomAdapter(getChildFragmentManager());
        ViewPager vp = (ViewPager) v.findViewById(R.id.pager);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * The idea to maintain the state between the two fragment is to save the policy
             * between the transitions.
             * It is the fragment which will decide if it needs to go till the IO or not.
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                EditorState.getInstance().setLoaded(((SaveInterface) adapter.getItem(position)).save());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return v;
    }

    private class CustomAdapter extends FragmentPagerAdapter{

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PolicyViewer.getInstance();
                case 1:
                    return PolicyTextEditor.getInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Policy Viewer";
                case 1:
                    return "Policy Editor";
                default:
                    return null;
            }
        }
    }

    // Quick and dirty design but sure of the unicity
    public static class EditorState {

        private static EditorState instance;

        public static EditorState getInstance(){
            if (instance == null){
                instance = new EditorState();
            }
            return instance;
        }
        File loaded;
        public File getLoaded() {
            return loaded;
        }

        public void setLoaded(File loaded) {
            this.loaded = loaded;
        }
    }


    // TODO Add icons to the menu and give the ability to display them as action
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.policy_editor_menu, menu);
    }
}