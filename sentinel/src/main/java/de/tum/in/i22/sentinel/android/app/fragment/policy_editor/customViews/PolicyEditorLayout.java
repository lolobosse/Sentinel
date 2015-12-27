package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.animation.LayoutTransition;
import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class PolicyEditorLayout extends LinearLayout implements PolicyChanger {

    Context c;
    Policy p;

    public PolicyEditorLayout(Context context, Policy p) {
        super(context);
        this.c = context;
        this.p = p;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        for (int i = 0; i < 5; i++) {
            ViewGroup v = (ViewGroup) inflate(c, R.layout.policy_expandable_list_view_layout, null);
            TextView tv = (TextView) v.findViewById(R.id.categoryTitle);
            switch (i) {
                case 0:
                    tv.setText("Description");
                    break;
                case 1:
                    tv.setText("Timestep");
                    break;
                case 2:
                    tv.setText("Triggers");
                    break;
                case 3:
                    tv.setText("Conditions");
                    break;
                case 4:
                    tv.setText("Authorization");
            }
            v.setOnClickListener(new CustomListener(v, i));
            addView(v);
        }
    }

    @Override
    public void onPolicyChange() {

    }

    private class CustomListener implements OnClickListener {

        boolean wasClicked = false;

        ViewGroup viewToBeAddedTo;
        int position;

        ArrayList<ViewGroup> viewsAdded;

        CustomListener(ViewGroup toBeAddedTo, int position) {
            this.position = position;
            this.viewToBeAddedTo = toBeAddedTo;
            viewsAdded = new ArrayList<>();
        }

        @Override
        public void onClick(View view) {
            if (!wasClicked) {
                switch (position) {
                    case 0:
                        createViewAndMakeVisible(new DescriptionLayout(getContext(), p, PolicyEditorLayout.this));
                        break;
                    case 1:
                        createViewAndMakeVisible(new TimeStepLayout(getContext(), p, PolicyEditorLayout.this));
                        break;
                    case 2:
                        createViewAndMakeVisible(new TriggerContainer(getContext(), p, PolicyEditorLayout.this));

                }
            }

            else {
                for (ViewGroup v : viewsAdded) {
                    v.setVisibility(GONE);
                }
                wasClicked = false;
            }
        }

        private void createViewAndMakeVisible(ViewGroup view1) {
            if (viewsAdded.isEmpty()) {
                viewsAdded.add(view1);
                viewToBeAddedTo.addView(view1);
            }
            for (ViewGroup v : viewsAdded) {
                v.setVisibility(VISIBLE);
            }
            wasClicked = true;
        }


    }


}
