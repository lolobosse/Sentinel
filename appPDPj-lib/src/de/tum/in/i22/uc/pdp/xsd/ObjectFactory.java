//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.12 at 01:48:04 PM CEST 
//


package de.tum.in.i22.uc.pdp.xsd;

import javax.xml.namespace.QName;

import ae.javax.xml.bind.JAXBElement;
import ae.javax.xml.bind.annotation.XmlElementDecl;
import ae.javax.xml.bind.annotation.XmlRegistry;
import de.tum.in.i22.uc.pdp.core.ParamMatch;
import de.tum.in.i22.uc.pdp.core.condition.operators.Always;
import de.tum.in.i22.uc.pdp.core.condition.operators.Before;
import de.tum.in.i22.uc.pdp.core.condition.operators.ConditionParamMatchOperator;
import de.tum.in.i22.uc.pdp.core.condition.operators.During;
import de.tum.in.i22.uc.pdp.core.condition.operators.EvalOperator;
import de.tum.in.i22.uc.pdp.core.condition.operators.EventMatchOperator;
import de.tum.in.i22.uc.pdp.core.condition.operators.OSLAnd;
import de.tum.in.i22.uc.pdp.core.condition.operators.OSLFalse;
import de.tum.in.i22.uc.pdp.core.condition.operators.OSLImplies;
import de.tum.in.i22.uc.pdp.core.condition.operators.OSLNot;
import de.tum.in.i22.uc.pdp.core.condition.operators.OSLOr;
import de.tum.in.i22.uc.pdp.core.condition.operators.OSLTrue;
import de.tum.in.i22.uc.pdp.core.condition.operators.RepLim;
import de.tum.in.i22.uc.pdp.core.condition.operators.RepMax;
import de.tum.in.i22.uc.pdp.core.condition.operators.RepSince;
import de.tum.in.i22.uc.pdp.core.condition.operators.Since;
import de.tum.in.i22.uc.pdp.core.condition.operators.StateBasedOperator;
import de.tum.in.i22.uc.pdp.core.condition.operators.Within;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.tum.in.i22.uc.pdp.xsd package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Condition_QNAME = new QName("http://www22.in.tum.de/enforcementLanguage", "condition");
    private final static QName _Policy_QNAME = new QName("http://www22.in.tum.de/enforcementLanguage", "policy");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.tum.in.i22.uc.pdp.xsd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConditionType }
     * 
     */
    public ConditionType createConditionType() {
        return new ConditionType();
    }

    /**
     * Create an instance of {@link PolicyType }
     * 
     */
    public PolicyType createPolicyType() {
        return new PolicyType();
    }

    /**
     * Create an instance of {@link AlwaysType }
     * 
     */
    public AlwaysType createAlwaysType() {
        return new Always();
    }

    /**
     * Create an instance of {@link OrType }
     * 
     */
    public OrType createOrType() {
        return new OSLOr();
    }

    /**
     * Create an instance of {@link AuthorizationInhibitType }
     * 
     */
    public AuthorizationInhibitType createAuthorizationInhibitType() {
        return new AuthorizationInhibitType();
    }

    /**
     * Create an instance of {@link RepMaxType }
     * 
     */
    public RepMaxType createRepMaxType() {
        return new RepMax();
    }

    /**
     * Create an instance of {@link PreventiveMechanismType }
     * 
     */
    public PreventiveMechanismType createPreventiveMechanismType() {
        return new PreventiveMechanismType();
    }

    /**
     * Create an instance of {@link TrueType }
     * 
     */
    public TrueType createTrueType() {
        return new OSLTrue();
    }

    /**
     * Create an instance of {@link WithinType }
     * 
     */
    public WithinType createWithinType() {
        return new Within();
    }

    /**
     * Create an instance of {@link ModifyActionType }
     * 
     */
    public ModifyActionType createModifyActionType() {
        return new ModifyActionType();
    }

    /**
     * Create an instance of {@link DelayActionType }
     * 
     */
    public DelayActionType createDelayActionType() {
        return new DelayActionType();
    }

    /**
     * Create an instance of {@link AuthorizationActionType }
     * 
     */
    public AuthorizationActionType createAuthorizationActionType() {
        return new AuthorizationActionType();
    }

    /**
     * Create an instance of {@link SinceType }
     * 
     */
    public SinceType createSinceType() {
        return new Since();
    }

    /**
     * Create an instance of {@link InitialRepresentationType }
     * 
     */
    public InitialRepresentationType createInitialRepresentationType() {
        return new InitialRepresentationType();
    }

    /**
     * Create an instance of {@link ContainerType }
     * 
     */
    public ContainerType createContainerType() {
        return new ContainerType();
    }

    /**
     * Create an instance of {@link DetectiveMechanismType }
     * 
     */
    public DetectiveMechanismType createDetectiveMechanismType() {
        return new DetectiveMechanismType();
    }

    /**
     * Create an instance of {@link ExecuteAsyncActionType }
     * 
     */
    public ExecuteAsyncActionType createExecuteAsyncActionType() {
        return new ExecuteAsyncActionType();
    }

    /**
     * Create an instance of {@link EvalOperatorType }
     * 
     */
    public EvalOperatorType createEvalOperatorType() {
        return new EvalOperator();
    }

    /**
     * Create an instance of {@link AndType }
     * 
     */
    public AndType createAndType() {
        return new OSLAnd();
    }

    /**
     * Create an instance of {@link NotType }
     * 
     */
    public NotType createNotType() {
        return new OSLNot();
    }

    /**
     * Create an instance of {@link ParameterType }
     * 
     */
    public ParameterType createParameterType() {
        return new ParameterType();
    }

    /**
     * Create an instance of {@link ConditionParamMatchType }
     * 
     */
    public ConditionParamMatchType createConditionParamMatchType() {
        return new ConditionParamMatchOperator();
    }

    /**
     * Create an instance of {@link DuringType }
     * 
     */
    public DuringType createDuringType() {
        return new During();
    }

    /**
     * Create an instance of {@link RepSinceType }
     * 
     */
    public RepSinceType createRepSinceType() {
        return new RepSince();
    }

    /**
     * Create an instance of {@link MechanismBaseType }
     * 
     */
    public MechanismBaseType createMechanismBaseType() {
        return new MechanismBaseType();
    }

    /**
     * Create an instance of {@link EventMatchingOperatorType }
     * 
     */
    public EventMatchingOperatorType createEventMatchingOperatorType() {
        return new EventMatchOperator();
    }

    /**
     * Create an instance of {@link ParamMatchType }
     * 
     */
    public ParamMatchType createParamMatchType() {
        return new ParamMatch();
    }

    /**
     * Create an instance of {@link RepLimType }
     * 
     */
    public RepLimType createRepLimType() {
        return new RepLim();
    }

    /**
     * Create an instance of {@link BeforeType }
     * 
     */
    public BeforeType createBeforeType() {
        return new Before();
    }

    /**
     * Create an instance of {@link StateBasedOperatorType }
     * 
     */
    public StateBasedOperatorType createStateBasedOperatorType() {
        return new StateBasedOperator();
    }

    /**
     * Create an instance of {@link ImpliesType }
     * 
     */
    public ImpliesType createImpliesType() {
        return new OSLImplies();
    }

    /**
     * Create an instance of {@link AuthorizationAllowType }
     * 
     */
    public AuthorizationAllowType createAuthorizationAllowType() {
        return new AuthorizationAllowType();
    }

    /**
     * Create an instance of {@link FalseType }
     * 
     */
    public FalseType createFalseType() {
        return new OSLFalse();
    }

    /**
     * Create an instance of {@link ExecuteActionType }
     * 
     */
    public ExecuteActionType createExecuteActionType() {
        return new ExecuteActionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConditionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www22.in.tum.de/enforcementLanguage", name = "condition")
    public JAXBElement<ConditionType> createCondition(ConditionType value) {
        return new JAXBElement<ConditionType>(_Condition_QNAME, ConditionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PolicyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www22.in.tum.de/enforcementLanguage", name = "policy")
    public JAXBElement<PolicyType> createPolicy(PolicyType value) {
        return new JAXBElement<PolicyType>(_Policy_QNAME, PolicyType.class, null, value);
    }

}