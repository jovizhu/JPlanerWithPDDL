

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
    private Vector<Action> mutexOps;

    /**
     * The pre props.
     */
    private Vector<Proposition> preProps;

    /**
     * The add props.
     */
    private Vector<Proposition> addProps;

    /**
     * The del props.
     */
    private Vector<Proposition> delProps;

    
    public Action(OperatorHead theHead, Conjunction thePre, Conjunction theAdd, Conjunction theDel) {
        opType = 0;
        actionName = theHead.opName;
        actionHead = theHead;
        preCondition = new Conjunction (thePre);
        addEffects = new Conjunction (theAdd);
        delEffects = new Conjunction (theDel);
        mutexOps = new Vector<Action>();
        preProps = new Vector<Proposition>();
        addProps = new Vector<Proposition>();
        delProps = new Vector<Proposition>();
    }

    public Action() {
		// TODO Auto-generated constructor stub
    	 opType = 0;
         actionHead = new OperatorHead();
         preCondition = new Conjunction ();
         addEffects = new Conjunction ();
         delEffects = new Conjunction ();
         mutexOps = new Vector<Action>();
         preProps = new Vector<Proposition>();
         addProps = new Vector<Proposition>();
         delProps = new Vector<Proposition>();
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

    public String getActionName() {
        return actionName;
    }


    public int getType() {
        return opType;
    }

    public Conjunction getPreConditions() {
        return preCondition;
    }

    public void setPreConditions(Conjunction conjunction) {
        preCondition = conjunction;
    }

    public Vector<Proposition> getPreProps() {
        return preProps;
    }

    public Conjunction getAddEffects() {
        return addEffects;
    }

    public void setAddEffects(Conjunction conjunction) {
        addEffects = conjunction;
    }

    public Vector<Proposition> getAddProps() {
        return addProps;
    }

    public Conjunction getDelEffects() {
        return delEffects;
    }

    public void setDelEffects(Conjunction conjunction) {
        delEffects = conjunction;
    }

    public Vector<Proposition> getDelProps() {
        return delProps;
    }

    public Vector<Action> getMutexOps() {
        return mutexOps;
    }

    public void setMutexOps(Vector<Action> vector) {
        mutexOps = vector;
    }

    public void addMutexOp(Action action) {
        if(!mutexOps.contains(action))
        {
            mutexOps.addElement(action);
            action.getMutexOps().addElement(this);
        }
    }

    public Action getMutexOp(int i) {
        return (Action)mutexOps.elementAt(i);
    }

    public boolean isMutex(Action action) {
        return mutexOps.contains(action);
    }

    public void addPreProp(Proposition proposition) {
        preProps.addElement(proposition);
    }

    public void addAddProp(Proposition proposition) {
        addProps.addElement(proposition);
    }

    public void addDelProp(Proposition proposition) {
        addProps.addElement(proposition);
    }

    public Proposition getPreProp(int i) {
        return (Proposition)preProps.elementAt(i);
    }

    public Proposition getAddProp(int i) {
        return (Proposition)addProps.elementAt(i);
    }

    public Proposition delPreProp(int i) {
        return (Proposition)delProps.elementAt(i);
    }

	public String toString() {
		String s = "\n"+actionName + "(";
		Iterator<Map.Entry<String, String>> it_para_entry = actionHead.parList
				.entrySet().iterator();
		if (it_para_entry.hasNext()) {
			while (it_para_entry.hasNext()) {
				Map.Entry<String, String> para_entry = it_para_entry.next();
				s += para_entry.getValue() + "=" + para_entry.getKey() + ", ";
			}
			s = s.substring(0, s.length() - 2);
		}
			s += ")";
		
		
		s += ("\tMutex With: ");
		int len = mutexOps.size();
		if (len>0) {
		for (int i = 0; i < len; i++) {
			Action act = (Action) mutexOps.elementAt(i);
			s += act.getActionName() + ", ";
		}
		s = s.substring(0, s.length() - 2);
		}

		return s;
	}

} // end Action
