

package graphplanner;

import java.util.*;


public class Action {

    // ------------------------------------------------------------------------
    // --- fields                                                           ---
    // ------------------------------------------------------------------------
	
	private OperatorHead  actionHead;
    /**
     * The pre condition.
     */
    private Conjunction preCondition;

    /**
     * The add effects.
     */
    private Conjunction addEffects;

    /**
     * The del effects.
     */
    private Conjunction delEffects;

    /**
     * The head.
     */
    private String actionName;

    /**
     * The op type.
     */
    private int opType;

    /**
     * The mutex ops.
     */
    private Vector mutexOps;

    /**
     * The pre props.
     */
    private Vector preProps;

    /**
     * The add props.
     */
    private Vector addProps;

    /**
     * The del props.
     */
    private Vector delProps;

    
    public Action(OperatorHead theHead, Conjunction thePre, Conjunction theAdd, Conjunction theDel) {
        opType = 0;
        actionName = theHead.opName;
        actionHead = theHead;
        preCondition = new Conjunction (thePre);
        addEffects = new Conjunction (theAdd);
        delEffects = new Conjunction (theDel);
        init();
    }

    public Action() {
		// TODO Auto-generated constructor stub
    	 opType = 0;
         actionHead = new OperatorHead();
         preCondition = new Conjunction ();
         addEffects = new Conjunction ();
         delEffects = new Conjunction ();
         init();
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public OperatorHead getActionHead() {
		return actionHead;
	}

	public void setActionHead(OperatorHead actionHead) {
		this.actionHead = actionHead;
	}

    // ------------------------------------------------------------------------
    // --- methods                                                          ---
    // ------------------------------------------------------------------------
    /**
     * Returns the header.
     *  
     * @return  The header.
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Returns the type.
     *  
     * @return  The type.
     */
    public int getType() {
        return opType;
    }

    /**
     * Returns the pre conditions.
     *  
     * @return  The pre conditions.
     */
    public Conjunction getPreConditions() {
        return preCondition;
    }

    /**
     * Sets the pre conditions.
     *  
     * @param conjunction The pre conditions.
     */
    public void setPreConditions(Conjunction conjunction) {
        preCondition = conjunction;
    }

    /**
     * Returns the pre props.
     *  
     * @return  The pre props.
     */
    public Vector getPreProps() {
        return preProps;
    }

    /**
     * Returns the add effects.
     *  
     * @return  The add effects.
     */
    public Conjunction getAddEffects() {
        return addEffects;
    }

    /**
     * Sets the add effects.
     *  
     * @param conjunction The add effects.
     */
    public void setAddEffects(Conjunction conjunction) {
        addEffects = conjunction;
    }

    /**
     * Returns the add props.
     *  
     * @return  The add props.
     */
    public Vector getAddProps() {
        return addProps;
    }

    /**
     * Returns the del effects.
     *  
     * @return  The del effects.
     */
    public Conjunction getDelEffects() {
        return delEffects;
    }

    /**
     * Sets the del effects.
     *  
     * @param conjunction The del effects.
     */
    public void setDelEffects(Conjunction conjunction) {
        delEffects = conjunction;
    }

    /**
     * Returns the del props.
     *  
     * @return  The del props.
     */
    public Vector getDelProps() {
        return delProps;
    }

    /**
     * Returns the mutex ops.
     *  
     * @return  The mutex ops.
     */
    public Vector getMutexOps() {
        return mutexOps;
    }

    /**
     * Sets the mutex ops.
     *  
     * @param vector The mutex ops.
     */
    public void setMutexOps(Vector vector) {
        mutexOps = vector;
    }

    /**
     * Adds a mutex op.
     *  
     * @param action The action.
     */
    public void addMutexOp(Action action) {
        if(!mutexOps.contains(action))
        {
            mutexOps.addElement(action);
            action.getMutexOps().addElement(this);
        }
    }

    /**
     * Returns the mutex op.
     *  
     * @param i The int.
     * @return  The mutex op.
     */
    public Action getMutexOp(int i) {
        return (Action)mutexOps.elementAt(i);
    }

    /**
     * Tests if ...
     *  
     * @param action The action.
     * @return  The boolean.
     */
    public boolean isMutex(Action action) {
        return mutexOps.contains(action);
    }

    /**
     * Adds a pre prop.
     *  
     * @param proposition The proposition.
     */
    public void addPreProp(Proposition proposition) {
        preProps.addElement(proposition);
    }

    /**
     * Adds a add prop.
     *  
     * @param proposition The proposition.
     */
    public void addAddProp(Proposition proposition) {
        addProps.addElement(proposition);
    }

    /**
     * Adds a del prop.
     *  
     * @param proposition The proposition.
     */
    public void addDelProp(Proposition proposition) {
        addProps.addElement(proposition);
    }

    /**
     * Returns the pre prop.
     *  
     * @param i The int.
     * @return  The pre prop.
     */
    public Proposition getPreProp(int i) {
        return (Proposition)preProps.elementAt(i);
    }

    /**
     * ...
     *  
     * @param i The int.
     * @return  The proposition.
     */
    public Proposition getaddProp(int i) {
        return (Proposition)addProps.elementAt(i);
    }

    /**
     * ...
     *  
     * @param i The int.
     * @return  The proposition.
     */
    public Proposition delPreProp(int i) {
        return (Proposition)delProps.elementAt(i);
    }

    /**
     * ...
     *  
     * @return  The string.
     */
    public String toString() {
        String s = actionName + "\n Mutex With: ";
        int len = mutexOps.size();
        for (int i = 0; i <len; i++)
        {
            Action act = (Action) mutexOps.elementAt(i);
            s += act.getActionName() + ", ";
        }
        return s;
    }

    /**
     * ...
     */
    private void init() {
        mutexOps = new Vector();
        preProps = new Vector();
        addProps = new Vector();
        delProps = new Vector();
    }

} // end Action
