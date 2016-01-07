package de.tum.in.i22.sentinel.android.app.fragment.policy_editor.customViews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tum.in.i22.sentinel.android.app.R;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.interfaces.PolicyChanger;
import de.tum.in.www22.enforcementlanguage.ConditionType;
import de.tum.in.www22.enforcementlanguage.DuringType;
import de.tum.in.www22.enforcementlanguage.Operators;
import de.tum.in.www22.enforcementlanguage.PolicyType;

/**
 * Created by laurentmeyer on 27/12/15.
 */
public class ConditionLayout extends LinearLayout {

    PolicyType p;
    PolicyChanger pc;
    // Need to extract it from the method because the method is recursive
    ArrayList<NamedMap> maps = new ArrayList<>();

    public ConditionLayout(Context context, PolicyType p, PolicyChanger pc) {
        super(context);
        this.p = p;
        this.pc = pc;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        ConditionType condition = p.getChoices().get(0).getPreventiveMechanism().getCondition();
        Operators o = condition.getConditionType();
        defineCondition(o);
        // Because of the recursion the element are not in order.
        Collections.reverse(maps);
        for(NamedMap m : maps){
            LinearLayout ll = (LinearLayout) inflate(getContext(), R.layout.condition_level_layout, null);
            TextView tv = (TextView) ll.findViewById(R.id.nameOfCondition);
            tv.setText(m.getName());
            Button plus = (Button) ll.findViewById(R.id.addCondition);
            plus.setOnClickListener(new PlusListener(maps.indexOf(m), condition));
            addView(ll);
            for (String key : m.keySet()) {
                ViewGroup vg = (ViewGroup) inflate(this.getContext(), R.layout.key_value_layout_tv, null);
                TextView keyTV = (TextView) vg.findViewById(R.id.key);
                TextView valueTV = (TextView) vg.findViewById(R.id.value);
                keyTV.setText(key);
                valueTV.setText(m.get(key));
                addView(vg);
            }
        }
    }

    private ArrayList<NamedMap> defineCondition(Object o) {
        try {
            Class c = o.getClass();
            Log.d("ConditionLayout", "c:" + c);
            Pattern p = Pattern.compile("(class de.tum.in.www22.enforcementlanguage.)([\\D]+Type)");
            for (Method m : c.getMethods()) {
                Matcher matcher = p.matcher(m.getReturnType().toString());
                String type = null;
                if (matcher.matches()) {
                    type = matcher.group(2);
                }
                if (type != null) {
                    Object typeFromGet = m.invoke(o);
                    if (typeFromGet != null) {
                        // We need to go a layer down
                        Class intermediateLevel = typeFromGet.getClass();
                        Object nextLevel = null;
                        for (Method m1 : intermediateLevel.getMethods()) {
                            if (m1.getReturnType().getSimpleName().equals("Operators")) {
                                nextLevel = m1.invoke(typeFromGet);
                            }
                        }
                        // Then next layer
                        if (nextLevel != null)
                            defineCondition(nextLevel);
                        // We reached the last layer so we can start to analyse the other getter and setter
                        String regexToExtractParamLabel = "(get)([\\D]+)";
                        Pattern p1 = Pattern.compile(regexToExtractParamLabel);
                        NamedMap map = new NamedMap();
                        for (Method m2 : intermediateLevel.getMethods()) {
                            map.setName(type.replace("Operator", "").replace("Type", ""));
                            if (!m2.getName().matches(regexToExtractParamLabel)) continue;
                            // To get the BlaBla of 'getBlaBla'
                            Matcher matcher1 = p1.matcher(m2.getName());
                            String label = matcher1.matches() ? matcher1.group(2) : null;
                            if (label == null) continue;
                            if (m2.getReturnType().equals(boolean.class)) {
                                map.put(label, String.valueOf(m2.invoke(typeFromGet)));
                            }
                            if (m2.getReturnType().equals(long.class)) {
                                map.put(label, String.valueOf(m2.invoke(typeFromGet)));
                            }
                            if (m2.getReturnType().equals(int.class)) {
                                map.put(label, String.valueOf(m2.invoke(typeFromGet)));
                            }
                            if (m2.getReturnType().equals(String.class)) {
                                map.put(label, String.valueOf(m2.invoke(typeFromGet)));
                            }
                        }
                        maps.add(map);
                    }
                    Log.d("ConditionLayout", "typeFromGet:" + typeFromGet);
                }
            }
        } catch (Exception e) {
            Log.d("ConditionLayout", "e:" + e);
        }
        return maps;
    }

    private void addALevelOfComplexity(Object parentOfTheNew, Object toBeAdded, Object childToBeMoved) throws InvocationTargetException, IllegalAccessException {
        Operators parentOpe = (Operators) parentOfTheNew;
        Log.d("ConditionLayout", "toBeAdded:" + toBeAdded);
        Operators addedOpe = getOperatorObjectFromCondition(toBeAdded);

        // Not to get the #IllegalAccessException
        parentOpe.clearOperatorsSelect();
        addedOpe.clearOperatorsSelect();


        // We need to get the name of the class of the chosen one to be able to add it properly to the parent
        Class addedClass = addedOpe.getClass();

        // We need to get the name of the class of the child to be able to add it properly to the added one
        Class childClass = childToBeMoved.getClass();

        String addedClassName = findClassNameOf(addedClass);
        String childClassName = findClassNameOf(childClass);

        setToNode(addedOpe, childToBeMoved, childClassName);
        setToNode(parentOpe, addedOpe, addedClassName);
    }

    private Operators getOperatorObjectFromCondition(Object o) throws InvocationTargetException, IllegalAccessException {
        Class toAnalyse = o.getClass();
        for (Method m : toAnalyse.getMethods()) {
            if (m.getName().contains("setOperators")) {
                Operators operators = new Operators();
                m.invoke(o, operators);
                Log.d("ConditionLayout", "o:" + ((DuringType) o).getOperators());
                return operators;
            }
        }
        return null;
    }

    /**
     * Iterate in all the methods of the receiver object to find the setter corresponding to the classname
     *
     * @param tobeAddedTo
     * @param toBeAdded
     * @param classOfToBeAdded
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void setToNode(Operators tobeAddedTo, Object toBeAdded, String classOfToBeAdded) throws IllegalAccessException, InvocationTargetException {
        Class aClass = tobeAddedTo.getClass();
        Method[] methods = aClass.getMethods();
        String tets = classOfToBeAdded;
        for (Method m : methods) {
            if (m.getName().contains(classOfToBeAdded.replace("Type", "").replace("Operators", "")) && m.getName().contains("set")) {
                m.invoke(tobeAddedTo, toBeAdded);
            }
        }
    }

    private String findClassNameOf(Class addedClass) {
        return addedClass.getSimpleName();
    }

    private Object getObjectAtThisLevel(int level, Operators c){
        try {
            Pattern p = Pattern.compile("(class de.tum.in.www22.enforcementlanguage.)([\\D]+Type)");
            int currentLevel = 0;
            Object toAnalyse = c;
            for (; currentLevel<=level; currentLevel++){
                toAnalyse = levelSearch(toAnalyse, p);
            }
            Log.d("ConditionLayout", "toAnalyse:" + toAnalyse);
            return toAnalyse;
        }catch (Exception e){

        }
        return null;
    }

    @Nullable
    private Object levelSearch(Object o, Pattern p) throws IllegalAccessException, InvocationTargetException {
        Class conditionClass = o.getClass();
        for (Method m : conditionClass.getMethods()) {
            Matcher matcher = p.matcher(m.getReturnType().toString());
            String type = null;
            if (matcher.matches()) {
                type = matcher.group(2);
            }
            if (type != null) {
                Object typeFromGet = m.invoke(o);
                if (typeFromGet != null) {
                    for (Method m1 : typeFromGet.getClass().getMethods()) {
                        if (m1.getReturnType().getSimpleName().equals("Operators")) {
                            Object toBeReturned = m1.invoke(typeFromGet);
                            if (toBeReturned != null){
                                return toBeReturned;
                            }
                        }
                    }
                    return typeFromGet;
                }
            }
        }
        return null;
    }

    private interface OnTypeChosen {
        void onTypeChosen(String name, Object type);
    }

    private static class NamedType {
        String name;
        Object type;

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class TypeChooser {

        private static Dialog createChooserDialog(Context c, Operators o, final OnTypeChosen otc) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
            AlertDialog.Builder b = new AlertDialog.Builder(c);
            final List<NamedType> allItems = getAllPossibleVariations(o);
            String[] allNames = new String[allItems.size()];
            for (NamedType item: allItems){
                allNames[allItems.indexOf(item)] = item.getName();
            }
            b.setItems(allNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    otc.onTypeChosen(allItems.get(which).getName(), allItems.get(which).getType());
                }
            });
            return b.create();
        }

        private static List<NamedType> getAllPossibleVariations(Operators o) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            ArrayList<NamedType> toBeReturned = new ArrayList<>();
            Class oClass = o.getClass();
            for (Method m : oClass.getMethods()) {
                if (m.getName().matches("get[\\D]+") && m.getReturnType().getSimpleName().matches("[\\D]+Type")) {
                    Pattern p = Pattern.compile("(class de.tum.in.www22.enforcementlanguage.)([\\D]+)Type");
                    Matcher matcher = p.matcher(m.getReturnType().toString());
                    if (matcher.matches()) {
                        NamedType type = new NamedType();
                        type.setName(matcher.group(2));
                        Class toBeInstantied = Class.forName(m.getReturnType().getName());
                        Object instantiedType = toBeInstantied.newInstance();
                        type.setType(instantiedType);
                        toBeReturned.add(type);
                    }
                }
            }
            return toBeReturned;
        }
    }

    private class PlusListener implements OnClickListener {

        int layerIndex;
        ConditionType ct;

        PlusListener(int layer, ConditionType c) {
            layerIndex = layer;
            ct = c;
        }

        @Override
        public void onClick(View view) {
            final Object parentOfTheNew = getObjectAtThisLevel(layerIndex, ct.getConditionType());
            final Object childOfTheNew = getObjectAtThisLevel(layerIndex + 1, ct.getConditionType());
            try {
                TypeChooser.createChooserDialog(getContext(), ct.getConditionType(), new OnTypeChosen() {
                    @Override
                    public void onTypeChosen(String name, Object type) {
                        Log.d("PlusListener", name);
                        try {
                            addALevelOfComplexity(parentOfTheNew, type, childOfTheNew);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // TODO: Create a middle state of the type the dialog defined
            // TODO: Create a dialog also with a regex and reflexion (on the #Operators class)
        }
    }

    private class NamedMap extends HashMap<String, String> {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
