
package graphplanner;



import java.util.*;


public class Conjunction {


	private ArrayList<State> states;


    public Conjunction() {
        states = new ArrayList<State>();
    }

    public Conjunction(Conjunction cnj){
    	states = new ArrayList<State>();
    	Iterator<State> it_states = cnj.states.iterator();
    	while(it_states.hasNext()){
    		State st = new State((State)it_states.next());
    		states.add(st);
    		
    	}
    }
    
    public Conjunction(State st){
    	states = new ArrayList<State>();
    	states.add(st);
    }
	
    public ArrayList<State> getStates() {
		return states;
	}



	public void setStates(ArrayList<State> states) {
		this.states = states;
	}



   
    public void add(State lit) {
        states.add (lit);
    }

   
    public State get(int index) {
        return (State) states.get(index);
    }

   
    public void remove(int index) {
        states.remove (index);
    }

   
    public boolean remove(State literal) {
        return states.remove (literal);
    }

    /**
     * adds theCnj to this conjunction and avoid duplication in states
     *  
     * @param theCnj The the cnj.
     */
    public void addConjunction(Conjunction theCnj) {

    	Iterator<State> it_cnj = theCnj.states.iterator();
    	while(it_cnj.hasNext()){
    		State cnj = it_cnj.next();
    		if(this.states.contains(cnj)){
    			this.states.add(cnj);
    		}
    	}
    }

   
    public int size() {
        return states.size();
    }

    public boolean contains(Conjunction theSub) {
       
        Iterator<State> it_cnj = theSub.states.iterator();
        while (it_cnj.hasNext())
        {
        	State cnj = it_cnj.next();
            if (this.contains(cnj) == false)
            	return false;
        }
        return true;
    }

  
    public boolean contains(State ast) {
    	Iterator<State> it_st = this.states.iterator();
    	while(it_st.hasNext()){
    		State st = it_st.next();
    		if(st.equals(ast)){
    			return true;
    		}
    	}
    	return false;
    }

   
    public boolean Intersect(Conjunction theCnj) {
    	Iterator<State> it_cnj = theCnj.states.iterator();
        while (it_cnj.hasNext())
        {
        	State cnj = it_cnj.next();
            if (this.contains ( cnj))
            	return true;
        }
        return false;
    }

  
    public boolean equal(Conjunction theCnj) {
        if (states.size() != theCnj.size())
        return false;
        int len = states.size();
        for (int i = 0; i < len; i++)
        {
            if (this.contains (theCnj.get(i)) == false)
            return false;
        }
        return true;
    }

    
    public String toString() {
        String s = new String();
        int size = states.size();
        if (size == 0)
        return null;
        for (int i = 0; i < size; i++)
        {
        	State st = states.get(i);
            s += st.getPredicate() +" (";
            Iterator<String> it_para = st.getParameter().iterator();
            while(it_para.hasNext()){
            	String para = it_para.next();
            	s +=para+" ";
            }
            s +=")\n";
        }
        return s;
    }

}
