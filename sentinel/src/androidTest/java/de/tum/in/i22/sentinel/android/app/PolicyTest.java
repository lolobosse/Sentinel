package de.tum.in.i22.sentinel.android.app;

import android.test.AndroidTestCase;

import java.util.ArrayList;

import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.AuthorizationAction;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Behavior;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Description;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.EventMatch;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.ParamMatch;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Policy;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.PreventiveMechanism;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.RepLim;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.RepLimCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.SuperCondition;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.TimeStep;
import de.tum.in.i22.sentinel.android.app.fragment.policy_editor.Trigger;

/**
 * Created by laurentmeyer on 23/12/15.
 */
public class PolicyTest extends AndroidTestCase {

    public void testGetterAndSetterOfPolicy() throws Exception {
        Policy p = new Policy();
        String name = "name";
        p.setName(name);
        boolean standalone = true;
        p.setStandalone(standalone);
        PreventiveMechanism mechanism = new PreventiveMechanism();
        p.setMechanism(mechanism);
        boolean deployed = true;
        p.setDeployed(deployed);

        assertEquals(name, p.getName());
        assertEquals(standalone, p.isStandalone());
        assertEquals(mechanism, p.getMechanism());
        assertEquals(deployed, p.isDeployed());
    }

    public void testDecoratorsOfThePolicy() throws Exception {
        Policy p = new Policy();
        String result = p.toString();

        assertTrue(result.contains("xmlns=\"http://www22.in.tum.de/enforcementLanguage\""));
        assertTrue(result.contains("xmlns:tns=\"http://www22.in.tum.de/enforcementLanguage\""));
        assertTrue(result.contains("xmlns:a=\"http://www22.in.tum.de/action\""));
        assertTrue(result.contains("xmlns:e=\"http://www22.in.tum.de/event\""));
        assertTrue(result.contains("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
    }

    /**
     * This test is interesting: it creates a policy exactly the same as the policy_appsms_duration_4
     * and compare it with a no space version of it.
     * @throws Exception
     */
    public void testPolicyAppDurationSMS4() throws Exception {
        String original = "<?xmlversion='1.0'standalone='yes'?><policyxmlns=\"http://www22.in.tum.de/enforcementLanguage\"xmlns:tns=\"http://www22.in.tum.de/enforcementLanguage\"xmlns:a=\"http://www22.in.tum.de/action\"xmlns:e=\"http://www22.in.tum.de/event\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"name=\"bliblablub\"><preventiveMechanismname=\"test2\"><description>NewPolicy</description><timestepamount=\"500\"unit=\"MILLISECONDS\"/><triggeraction=\"sentTextMessage\"tryEvent=\"true\"><paramMatchname=\"destination\"value=\"12345\"/></trigger><condition><not><repLimamount=\"20\"unit=\"SECONDS\"lowerLimit=\"0\"upperLimit=\"1\"><eventMatchaction=\"sentTextMessage\"tryEvent=\"true\"><paramMatchname=\"destination\"value=\"12345\"/></eventMatch></repLim></not></condition><authorizationActionname=\"default\"><inhibit/></authorizationAction></preventiveMechanism></policy>";

        Policy p = new Policy();
        p.setName("bliblablub");
        p.setStandalone(true);

        PreventiveMechanism pm = new PreventiveMechanism();
        pm.setName("test2");

        Description d = new Description();
        d.setDescription("New Policy");
        pm.setDescription(d);

        TimeStep t = new TimeStep();
        t.setAmount(500);
        t.setUnit("MILLISECONDS");
        pm.setTimestep(t);

        ArrayList triggers = new ArrayList<>();
        Trigger trigger = new Trigger();
        trigger.setAction("sentTextMessage");
        trigger.setTryEvent(true);
        triggers.add(trigger);
        ParamMatch paramMatch = new ParamMatch();
        paramMatch.setName("destination");
        paramMatch.setValue("12345");
        ArrayList<ParamMatch> paramMatches = new ArrayList<>();
        paramMatches.add(paramMatch);
        trigger.setParamMatches(paramMatches);
        pm.setTriggers(triggers);

        RepLimCondition repLimConditions = new RepLimCondition();
        ArrayList<RepLim> replims = new ArrayList<>();
        repLimConditions.setIsNot(true);
        RepLim repLim = new RepLim();
        repLim.setUnit("SECONDS");
        repLim.setAmount(20);
        repLim.setLowerLimit(0);
        repLim.setUpperLimit(1);
        replims.add(repLim);
        EventMatch e = new EventMatch();
        e.setAction("sentTextMessage");
        e.setTryEvent(true);
        ParamMatch paramMatch1 = new ParamMatch();
        paramMatch1.setName("destination");
        paramMatch1.setValue("12345");
        ArrayList<ParamMatch> paramMatches1 = new ArrayList<>();
        paramMatches1.add(paramMatch1);
        e.setParamMatches(paramMatches1);
        ArrayList<EventMatch> eventMatches = new ArrayList<>();
        eventMatches.add(e);
        repLim.setMatches(eventMatches);
        repLimConditions.setRepLims(replims);
        ArrayList<SuperCondition> allConditions = new ArrayList<>();
        allConditions.add(repLimConditions);
        pm.setConditions(allConditions);

        AuthorizationAction action = new AuthorizationAction();
        action.setName("default");
        // TODO: Make that better
        action.setB(new Behavior());
        pm.setAuthorizationAction(action);

        p.setMechanism(pm);
        String result = p.toString();
        result = result.replace(" ", "");
        result = result.replace("\n", "");
        assertEquals(original, result);
    }
}
