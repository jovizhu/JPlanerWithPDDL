package graphplanner;

import java.io.*;
import java.util.*;


public class Proposition {

    // ------------------------------------------------------------------------
    // --- fields                                                           ---
    // ------------------------------------------------------------------------
    /**
     * The pre_list.
     */
    Vector pre_list;

    /**
     * The add_list.
     */
    Vector add_list;

    /**
     * The del_list.
     */
    Vector del_list;

    /**
     * The mutex_props.
     */
    Vector mutex_props;

    /**
     * The name.
     */
    //private String name;
    
    public State state;


    // ------------------------------------------------------------------------
    // --- constructor                                                      ---
    // ------------------------------------------------------------------------
    /**
     * Creates a new instance of Proposition.
     *  
     * @param theName The the name.
     */
    public Proposition(State st) {
        state = st;
        pre_list = new Vector();
        add_list = new Vector();
        del_list = new Vector();
        mutex_props = new Vector();
    }


    // ------------------------------------------------------------------------
    // --- methods                                                          ---
    // ------------------------------------------------------------------------
    /**
     * Returns the name.
     *  
     * @return  The name.
     */
    public State getState() {
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
    public Vector getPreConditions() {
        return pre_list;
    }

    /**
     * Returns the add effects.
     *  
     * @return  The add effects.
     */
    public Vector getAddEffects() {
        return add_list;
    }

    /**
     * Returns the del effects.
     *  
     * @return  The del effects.
     */
    public Vector getDelEffects() {
        return del_list;
    }

    /**
     * Returns the mutex props.
     *  
     * @return  The mutex props.
     */
    public Vector getMutexProps() {
        return mutex_props;
    }

    /**
     * Returns the mutex prop.
     *  
     * @param index The index.
     * @return  The mutex prop.
     */
    public Proposition getMutexProp(int index) {
        return (Proposition) mutex_props.elementAt(index);
    }

    /**
     * sets a mutex relation between me and theProp and vise versa.
     *  
     * @param theProp The the prop.
     */
    public void addMutexProp(Proposition theProp) {
        if (mutex_props.contains (theProp) == false) // check that I don't know this fact
        {
            mutex_props.addElement (theProp);
            theProp.getMutexProps().addElement (this);
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

    /**
     * ...
     *  
     * @return  The string.
     */
    public String toString() {
        String s = new String();
        s += "\nProposition: " + state.toString();
        s += "\n Mutex with: ";
        for (int i = 0; i < mutex_props.size(); i++)
        {
            s += getMutexProp(i).state.toString()+ ", ";
        }
        return s;
    }

} // end Proposition
