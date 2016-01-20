package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.policy_editor_enrico_way;

import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by laurentmeyer on 19/01/16.
 */
public class PolicyTextEditor extends Fragment implements SaveInterface{

    private static PolicyTextEditor instance;

    // Seems to be reliably called every time the fragment is called so it shouldn't be a problem to rely on that
    public static PolicyTextEditor getInstance() {
        if (instance == null) {
            instance = new PolicyTextEditor();
        }
        return instance;
    }


    @Override
    public File save() {
        return null;
    }
}
