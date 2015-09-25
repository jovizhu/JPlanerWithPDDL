

package graphplanner;

import graphplanner.Action;
import graphplanner.Proposition;
import graphplanner.PropositionLayer;


public class Goal {

    /**
     * The ntried.
     */
    int ntried;

    /**
     * The achieved.
     */
    boolean achieved;
    
    private Proposition proposition;
    /**
     * The nactions.
     */
    private int nactions;



  
    public Goal() {
		super();
		// TODO Auto-generated constructor stub
		ntried = 0;
		achieved = false;
		nactions = 0;
		
	}

/*    public Goal(State state, PropositionLayer theLayer) {
    	proposition = theLayer.getProposition (state);
        nactions = proposition.getAddEffects().size();
        ntried = 0;
        achieved = false;
    }*/
    public Goal(String state, PropositionLayer theLayer) {
    	proposition = theLayer.getProposition (state);
        nactions = proposition.getAddEffects().size();
        ntried = 0;
        achieved = false;
        }
    
    
    public Action nextAction() {
        if (ntried < nactions)
        {
            ntried++;
            return (Action) proposition.getAddEffects().elementAt(ntried-1);
        }
        return null;
    }
    
	public int getNActions() {
        return nactions;
    }

   
    public boolean isAchieved() {
        return achieved;
    }

    public void achieved(boolean value) {
        achieved = value;
    }
 
    void init() {
        ntried = 0;
    }

} // end Goal
