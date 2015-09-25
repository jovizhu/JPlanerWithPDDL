package graphplanner;

import java.util.*;
import java.util.Map.Entry;


public class PropositionLayer {

    // ------------------------------------------------------------------------
    // --- fields                                                           ---
    // ------------------------------------------------------------------------
    /**
     * previous and next action layers
     */
    private ActionLayer prev=null;

    /**
     * The next.
     */
    private ActionLayer next=null;

    /**
     * hashtable for all propositions in that layer
     */
    public HashMap<String, Proposition> propositions;

    /**
     * The propositions_vector.
     */
    private Vector<Proposition> propositions_vector;

    /**
     * a conjunction object formed from all the propositions in the layer
     */
    private Conjunction conjunction=null;


    // ------------------------------------------------------------------------
    // --- constructors                                                     ---
    // ------------------------------------------------------------------------
    /**
     * Creates a new instance of PropositionLayer.
     */
    public PropositionLayer() {
        propositions = new  HashMap<String, Proposition>();
        conjunction = new Conjunction();
        propositions_vector = new Vector<Proposition>(); 
    }

    /**
     * Creates a new instance of PropositionLayer.
     *  
     * @param theprev The theprev.
     */
    public PropositionLayer(ActionLayer theprev) {
        this.prev = theprev;
        propositions = new HashMap<String, Proposition>();
        conjunction = new Conjunction();
        propositions_vector = new Vector<Proposition>();
    }


    // ------------------------------------------------------------------------
    // --- methods                                                          ---
    // ------------------------------------------------------------------------
    /**
     * Sets the next layer.
     *  
     * @param theNext The next layer.
     */
    public void setNextLayer(ActionLayer theNext) {
        next = theNext;
    }

    /**
     * Returns the next layer.
     *  
     * @return  The next layer.
     */
    public ActionLayer getNextLayer() {
        return next;
    }

    /**
     * Returns the prev layer.
     *  
     * @return  The prev layer.
     */
    public ActionLayer getPrevLayer() {
        return prev;
    }

    /**
     * Returns the proposition.
     *  
     * @param key The key.
     * @return  The proposition.
     */
    public Proposition getProposition(String key) {
        return (Proposition) propositions.get (key);
    }

    /**
     * Returns the proposition.
     *  
     * @param index The index.
     * @return  The proposition.
     */
    public Proposition getProposition(int index) {
        return (Proposition) propositions_vector.elementAt (index);
    }

    /**
     * Returns the conjunction.
     *  
     * @return  The conjunction.
     */
    public Conjunction getConjunction() {
        return conjunction;
    }

    /**
     * If the proposition with the same name is already in this layer
     * return the existing proposition.
     * else
     * add the new proposition and return it.
     *  
     * @param thePro The the pro.
     * @return  The proposition.
     */
    public Proposition addProposition(Proposition thePro) {
        // first check that it doesn't exixt
        if (propositions.containsKey(thePro.state)){
        	return (Proposition) propositions.get (thePro.state);
        }
        propositions.put (thePro.state, thePro);
        
        conjunction.add(thePro.state);
        return thePro;
    }
    
    public Proposition addProposition(String st) {
        // first check that it doesn't exixt
        if (propositions.containsKey(st)){
        	return (Proposition) propositions.get (st);
        }
        Proposition prop = new Proposition(st);
        propositions.put (st, prop);
        
        conjunction.add(prop.state);
        return prop;
    }

    /**
     * ...
     *  
     * @return  The int.
     */
    public int size() {
        return propositions.size();
    }

    /**
     * Called to construct the initial state proposition layer
     *  
     * @param theConjunction The init layer.
     */
    public void setInitLayer(Conjunction theCnj) {
        Iterator<String> it_cnj = theCnj.getStates().iterator();
        while(it_cnj.hasNext()){
        	String st = it_cnj.next();
        	this.addProposition(st);
        }
        
        Iterator<Map.Entry<String, Proposition>> it_prop = this.propositions.entrySet().iterator();
        while(it_prop.hasNext()){
        	Map.Entry<String, Proposition> prop = it_prop.next();
        	this.propositions_vector.add(prop.getValue());
        }
    }

    /**
     * ...
     *  
     * @param theLayer The the layer.
     * @return  The boolean.
     */
    public boolean equal(PropositionLayer theLayer) {
        return conjunction.equal (theLayer.getConjunction());
    }

    /**
     * determine and set all the mutex relations among propositions in that layer
     */
    public void calculateMutex() {
        createPropositionVector();
        int len = propositions_vector.size();
        for (int prop = 0; prop < len ; prop++) // for each prop in that layer
        {
            Proposition pro1 = getProposition (prop);
            for (int otherprop = prop + 1; otherprop < len; otherprop++)
            {
                Proposition pro2 = getProposition (otherprop);
                checkMutex (pro1,pro2);
            }
        }
    }

    /**
     * ...
     *  
     * @return  The string.
     */
    public String toString() {
        String s = new String();
        for (int i = 0 ; i < propositions_vector.size(); i++)
        s += "Proposition: "+getProposition(i).toString()+"\n";
        return s;
    }

    /**
     * important for building createPropositionVector to retrieve props by index
     */
    private void createPropositionVector() {
        propositions_vector = new Vector<Proposition>();
        
        
        Iterator<Entry<String, Proposition>> it_prop = propositions.entrySet().iterator();
        while(it_prop.hasNext()){
        	Map.Entry<String, Proposition> pair = (Entry<String, Proposition>) it_prop.next();
        	propositions_vector.add(pair.getValue());
        }
       
    }

    /**
     * ...
     *  
     * @param p1 The proposition.
     * @param p2 The proposition.
     */
    private void checkMutex(Proposition p1, Proposition p2) {
        Vector p1Actions = p1.getAddEffects();
        Vector p2Actions = p2.getAddEffects();
        int len1 = p1Actions.size();
        int len2 = p2Actions.size();
        // if all actions supporting p1 are mutex with actions supporting p2
        // then p1 is mutex with p2.
        for (int i = 0 ; i < len1; i++)
        {
            Action op1 = (Action) p1Actions.elementAt(i);
            for (int j = 0; j < len2; j++)
            {
                Action op2 = (Action) p2Actions.elementAt(j);
                if( op1.isMutex(op2) == false)
                return;
                // at least there is one path
            }
        }
        p1.addMutexProp(p2);
    }

} // end PropositionLayer
