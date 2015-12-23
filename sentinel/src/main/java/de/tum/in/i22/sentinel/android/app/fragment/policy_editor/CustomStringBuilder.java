package de.tum.in.i22.sentinel.android.app.fragment.policy_editor;


/**
 * Created by laurentmeyer on 23/12/15.
 */
public class CustomStringBuilder {

    StringBuilder b;

    CustomStringBuilder(){
        b = new StringBuilder();
    }

    public StringBuilder append(Object append){
        if (append != null){
            return b.append(append);
        }
        return b;
    }

    @Override
    public String toString() {
        return b.toString();
    }
}
