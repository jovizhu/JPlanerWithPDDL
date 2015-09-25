package graphplanner;

import java.util.*;


public class Proposition {

    // ------------------------------------------------------------------------
    // --- fields                                                           ---
    // ------------------------------------------------------------------------
    /**
     * The pre_list.
     */
    Vector<Action> pre_list;

    /**
     * The add_list.
     */
    Vector<Action> add_list;

    /**
     * The del_list.
     */
    Vector<Action> del_list;

    /**
     * The mutex_props.
     */
    Vector<String> mutex_props;

    /**
     * The name.
     */
    //private String name;
    
    public String state;


    // ------------------------------------------------------------------------
    // --- constructor                                                      ---
    // ------------------------------------------------------------------------
    /**
     * Creates a new instance of Proposition.
     *  
     * @param theName The the name.
     */
    public Proposition(String st) {
        state = st;
        pre_list = new Vector<Action>();
        add_list = new Vector<Action>();
        del_list = new Vector<Action>();
        mutex_props = new Vector<String>();
    }


    // ------------------------------------------------------------------------
    // --- methods                                                          ---
    // ------------------------------------------------------------------------
    /**
     * Returns the name.
     *  
     * @return  The name.
     */
    public String getState() {
        return state;
    }

    /**
     * ...
     *  
     * @param theAct The the act.
     */
    public void putPreEdge(Action theAct) {
        pre_list.addElement (theAct);
    }

    /**
     * ...
     *  
     * @param theAct The the act.
     * @param noop The noop.
     */
    public void putAddEdge(Action theAct, int noop) {
        if (noop == 1)
        add_list.insertElementAt (theAct, 0);
        // put noop at the top
        else
        add_list.addElement (theAct);
    }

    /**
     * ...
     *  
     * @param theAct The the act.
     */
    public void putDelEdge(Action theAct) {
        del_list.addElement (theAct);
    }

    /**
     * Returns the pre conditions.
     *  
     * @return  The pre conditions.
     */
    public Vector<Action> getPreConditions() {
        return pre_list;
    }

    /**
     * Returns the add effects.
     *  
     * @return  The add effects.
     */
    public Vector<Action> getAddEffects() {
        return add_list;
    }

    /**
     * Returns the del effects.
     *  
     * @return  The del effects.
     */
    public Vector<Action> getDelEffects() {
        return del_list;
    }

    /**
     * Returns the mutex props.
     *  
     * @return  The mutex props.
     */
    public Vector<String> getMutexProps() {
        return mutex_props;
    }

    /**
     * Returns the mutex prop.
     *  
     * @param index The index.
     * @return  The mutex prop.
     */
    public String getMutexProp(int index) {
        return (String) mutex_props.elementAt(index);
    }

    /**
     * sets a mutex relation between me and theProp and vise versa.
     *  
     * @param theProp The the prop.
     */
    public void addMutexProp(Proposition theProp) {
        if (mutex_props.contains (theProp.state) == false) // check that I don't know this fact
        {
            mutex_props.addElement (theProp.state);
            theProp.getMutexProps().addElement (this.state);
        }
    }

    /**
     * Tests if ...
     *  
     * @param theProp The the prop.
     * @return  The boolean.
     */
    public boolean isMutex(Proposition theProp) {
        return mutex_props.contains (theProp);
    }
    
    public boolean mutexEqual(Proposition prop2){
    	Vector<String> m1 = this.getMutexProps();
    	Vector<String> m2 = prop2.getMutexProps();
    	
    	if(m1.size() != m2.size()) return false;
    	
    	for(int i=0; i<m1.size(); i++){
    		String mp1 = m1.get(i);
    		if(!m2.contains(mp1)){
    			return false;
    		}
    	} 	
    	
    	return true;
    }

    /**
     * ...
     *  
     * @return  The string.
     */
    public String toString() {
        String s = new String();
        s += state.toString();
        s += "\n\tMutex with: ";
        for (int i = 0; i < mutex_props.size(); i++)
        {
            s += getMutexProp(i).toString()+ "  ";
        }
        return s;
    }

} // end Proposition
