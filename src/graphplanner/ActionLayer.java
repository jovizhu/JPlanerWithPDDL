package graphplanner;

import java.util.*;


public class ActionLayer {
	
	// ------------------------------------------------------------------------
    // --- fields                                                           ---
    // ------------------------------------------------------------------------
    /**
     * The pos.
     */
    int pos;

    /**
     * previous proposition layers
     */
    private PropositionLayer prevPropositionLayer=null;

    /**
     * The next proposition layers
     */
    private PropositionLayer nextPropositionLayer=null;

    /**
     * hashtable for all propositions in that layer
     */
    private HashMap<OperatorHead, Action> actions;

    /**
     * The actions_vector.
     */
    private Vector<Action> actions_vector;

    /**
     * The conjunction.
     */
    private Conjunction conjunction;

    /**
     * The applicable.
     */
    private Vector<Action> applicable=null;

    /**
     * The my goals.
     */
    private Vector<Goal> myGoals=null;


    // ------------------------------------------------------------------------
    // --- constructors                                                     ---
    // ------------------------------------------------------------------------

    public ActionLayer() {
        actions = new HashMap<OperatorHead, Action>();
        conjunction = new Conjunction();
    }

    public ActionLayer(PropositionLayer thePrev, Vector<Action> appActions) {
    	prevPropositionLayer = thePrev;
        nextPropositionLayer = new PropositionLayer (this);
        // then init actions and conjunction
        actions = new HashMap<OperatorHead, Action>();
        conjunction = new Conjunction();
        int len = appActions.size();
        for (int i = 0; i < len; i++)
        {
            Action act = (Action) appActions.elementAt(i);
            actions.put (act.getActionHead(), act);
            //establish precondition edges
            ConnectPreEdges (act);
            //create and establish add / del edges
            ConnectADEdges (act);
        }
        // add no-ops
        addNoops();
        // calculate mutex
        calculateMutex();
        // in this action layer
        nextPropositionLayer.calculateMutex();
    }


    // ------------------------------------------------------------------------
    // --- methods                                                          ---
    // ------------------------------------------------------------------------

    public Conjunction getConjunction() {
        return conjunction;
    }


    public int size() {
        return actions.size();
    }

    public boolean equal(ActionLayer theLayer) {
        return conjunction.equal (theLayer.getConjunction());
    }


    public void calculateMutex() {
        createActionVector();
        int len = actions.size();
        for (int act = 0; act < len; act++)		// for each action
        {
            Action op1 = getAction (act);
            for (int act2 = act + 1; act2 < len; act2++) 	// for each 2nd action
            {
                Action op2 = getAction (act2);
                if ( checkEffects (op1, op2))
                continue;
                else if ( checkPre (op1, op2))
                continue;
                else if ( checkPreMutex (op1, op2))
                continue;
            }
        }
    }

    public String toString() {
        String s = new String ();
        s += "\n Action Layer\n\t";
        for (int i = 0; i < actions_vector.size(); i++)
        	s += getAction(i).toString()+"  ";
        return s;
    }

    public PropositionLayer getPrevLayer() {
        return prevPropositionLayer;
    }

    public PropositionLayer getNextLayer() {
        return nextPropositionLayer;
    }

    public Action getAction(String key) {
        return (Action) actions.get (key);
    }

    public Action getAction(int index) {
        return (Action) actions_vector.elementAt (index);
    }

    @SuppressWarnings("unused")
	public boolean searchTheLevel(Conjunction theGoals) {
        // TODO: test for memoized goals. if found return -1
        if (myGoals == null || myGoals.size() == 0)
        {
            applicable = new Vector<Action>();
            myGoals = new Vector<Goal>();
            pos = 0;
            int len = theGoals.size();
            for (int i = 0; i < len; i++)
            myGoals.addElement( new Goal (theGoals.get(i), nextPropositionLayer) );
        }
        int len = myGoals.size();
        for (int i = pos; i < len; i++)		// for each remaining goal
        {
            Goal g = (Goal) myGoals.elementAt (i);
            boolean achieved = false;
            g.init();
            for (int j = 0; j < g.getNActions(); j++)	// for each action supports that goal
            {
                Action op = g.nextAction();
                if ( addApplicableAction(op)) {
                    achieved = true;
                    pos++;
                    if (searchTheLevel (theGoals)) {
                    	return true;
                    } else {
                    	// try another action supports that goal
                        pos--;
                        achieved = false;
                        removeApplicableAction();
                    }
                } // end if
            }	// end j loop
            return false;
        }	// end i loop
        return true;
    }

    public void printApplicable() {
        if (applicable == null)
        {
            System.out.println("No applicables");
            return;
        }
        int len = applicable.size();
        System.out.println ("APPLICABLE ACTIONS ARE:" + len);
        for(int i =0; i < len; i++)
        {
            Action op = (Action) applicable.elementAt(i);
            System.out.println (op.getActionHead());
        }
    }

    /**
     * returns a vector of applicable actions at this layer
     * and remove any duplicated actions
     * before calling it you must be sure you get a valid plan
     *  
     * @return  The applicable actions.
     */
    public Vector<Action> getApplicableActions() {
        Vector<Action> rVal = new Vector<Action>();
        int len = applicable.size();
        for(int i =0; i < len; i++)
        {
            Action op = (Action) applicable.elementAt(i);
            String item = op.getActionName();
            if (item.indexOf("noop") < 0)
            rVal.addElement (op);
        }
        // now remove any duplicated entries
        for (int i = 0; i < rVal.size() ; i++)
        {
        	Action s1 = (Action) rVal.elementAt(i);
            for (int j = i + 1; j < rVal.size();)
            {
            	Action s2 = (Action) rVal.elementAt(j);
                if (s1.equals(s2)){
                	rVal.removeElementAt(j);
                }else{
                	j++;
                }
            }
        }
        return rVal;
    }

    public boolean searchPlan(Conjunction theGoals) {
        // if goals are memoized return no plan, null.
        if (this.searchTheLevel (theGoals)) // search this level
        {
            // determine new goal set
            Conjunction newGoals = new Conjunction();
            int len = applicable.size();
            for (int i = 0; i < len; i++)
            {
                Action op = (Action) applicable.elementAt(i);
                newGoals.addConjunction (op.getPreConditions());
                // remove duplicated goals
            }
            // search a plan for the new goal in next app layer
            ActionLayer app = prevPropositionLayer.getPrevLayer();
            if (app == null)	// no more layers we reached the init state
            return true;
            if ( app.searchPlan (newGoals) )
            return true;
            // plan is found
            else
            return false;
        }
        else
        {
            // memoize this set of goals
            return false;
            // no plan
        }
    }

    HashMap<OperatorHead, Action> getActions() {
        return actions;
    }

    /**
     * connect the operator theAct to it's precondition propositions
     * in the prev layer.
     */
    private void ConnectPreEdges(Action theAct) {
        Conjunction precondition = theAct.getPreConditions();
        Proposition temp = null;
        int len = precondition.size();
        for (int i = 0; i < len; i++)
        {
            String s = precondition.get(i);
            temp = prevPropositionLayer.getProposition (s);
            temp.putPreEdge (theAct);
            theAct.addPreProp (temp);
        }
    }

    /**
     * connect the operator theAct to it's add/del propositions
     * in the next layer.
     */
    private void ConnectADEdges(Action theAct) {
        Conjunction add = theAct.getAddEffects();
        Proposition temp = null;
        int len1 = add.size();
        // add edges
        for (int i = 0; i < len1; i++)
        {
            temp = nextPropositionLayer.addProposition ( add.get(i) );
            temp.putAddEdge (theAct, 0);
            theAct.addAddProp (temp);
        }
        // del edges
        Conjunction del = theAct.getDelEffects();
        int len2 = del.size();
        for (int j = 0; j < len2; j++)
        {
            temp = nextPropositionLayer.addProposition ( del.get(j) );
            temp.putDelEdge (theAct);
            theAct.addDelProp (temp);
        }
    }

    /**
     * add NOOPs to this layer
     */
    private void addNoops() {
        Conjunction cnj = prevPropositionLayer.getConjunction();
        int len = cnj.size();
        for (int i = 0; i < len; i++)
        {
            // create noop object
            String s = new String ("noop-"+ cnj.get(i));
            Action temp = new Action();
            temp.setActionName(s);
            temp.getActionHead().setOpName(s);
            temp.setPreConditions (new Conjunction (cnj.get(i)));
            temp.setAddEffects (new Conjunction (cnj.get(i)));
            // add noop to this action layer
            actions.put (temp.getActionHead(), temp);
            conjunction.add(s);
            // connect precondition edge
            Proposition p = prevPropositionLayer.getProposition (cnj.get(i));
            p.putPreEdge (temp);
            temp.addPreProp (p);
            p = nextPropositionLayer.addProposition (cnj.get(i));
            p.putAddEdge(temp, 1);
            temp.addAddProp (p);
        }
    }

    /**
     * this action vector is needed to retrieve actions based on an index value
     */
    private void createActionVector() {
        actions_vector = new Vector<Action>();
        Iterator<Map.Entry<OperatorHead, Action>> it_actions = actions.entrySet().iterator();
        while(it_actions.hasNext()){
        	Map.Entry<OperatorHead, Action> action = it_actions.next();
        	actions_vector.add(action.getValue());
        }
    }

    /**
     * first effect negates second effect or vise versa
     */
    private boolean checkEffects(Action op1, Action op2) {
        Conjunction c1 = op1.getAddEffects();
        Conjunction c2 = op2.getDelEffects();
        if (c1.size() > 0 && c2.size() > 0)
        if (c1.Intersect (c2))
        {
            op1.addMutexOp (op2);
            return true;
        }
        c1 = op1.getDelEffects();
        c2 = op2.getAddEffects();
        if (c1.size() > 0 && c2.size() > 0)
        if (c1.Intersect (c2))
        {
            op1.addMutexOp (op2);
            return true;
        }
        return false;
    }

    /**
     * first op negates precondition in the second op or vise versa
     *  
     * @param op1 The op1.
     * @param op2 The op2.
     * @return  The boolean.
     */
    private boolean checkPre(Action op1, Action op2) {
        Conjunction c1 = op1.getDelEffects();
        Conjunction c2 = op2.getPreConditions();
        if (c1.size() > 0 && c2.size() > 0)
        if (c1.Intersect (c2))
        {
            op1.addMutexOp (op2);
            return true;
        }
        c1 = op1.getPreConditions();
        c2 = op2.getDelEffects();
        if (c1.size() > 0&& c2.size() > 0)
        if (c2.Intersect (c1))
        {
            op1.addMutexOp (op2);
            return true;
        }
        return false;
    }

    private boolean checkPreMutex(Action op1, Action op2) {
        // build two (Proposition) Vectors of preconditions of op1 and op2
        // check mutex relations among propositions
        Vector<Proposition> pre1 = op1.getPreProps();
        Vector<Proposition> pre2 = op2.getPreProps();
        int len1 = pre1.size();
        int len2 = pre2.size();
        for (int i = 0; i < len1; i++)
        {
            Proposition p1 = (Proposition) pre1.elementAt(i);
            for (int j = 0; j < len2; j++)
            {
                Proposition p2 = (Proposition) pre2.elementAt(j);
                if (p1.isMutex(p2))
                {
                    op1.addMutexOp (op2);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Note: an operator may be added more than once so you must take this into
     * your account when generating the plan
     *  
     * @param theAct The the act.
     * @return  The boolean.
     */
    private boolean addApplicableAction(Action theAct) {
    /**
     * add theAct to applicable if it's not mutex with any applicable action
     * Note an operator may be added twice. 
     */
        int len = applicable.size();
        for (int i = 0; i < len; i++)
        {
            Action op = (Action) applicable.elementAt(i);
            if ( op.isMutex (theAct) )
            return false;
        }
        applicable.addElement (theAct);
        return true;
    }

    /**
     * Removes a applicable action.
     */
    private void removeApplicableAction() {
        if (applicable.size() > 0)
        applicable.removeElementAt(applicable.size()-1);
        else
        System.out.println ("No More applicable actions" );
    }

} // end ActionLayer
