
package graphplanner;



import java.util.*;


public class Conjunction {


	private ArrayList<String> states;


    public Conjunction() {
        states = new ArrayList<String>();
    }

    public Conjunction(Conjunction cnj){
    	states = new ArrayList<String>();
    	Iterator<String> it_states = cnj.states.iterator();
    	while(it_states.hasNext()){
    		String st = new String((String)it_states.next());
    		states.add(st);
    		
    	}
    }
    
    public Conjunction(String st){
    	states = new ArrayList<String>();
    	states.add(st);
    }
	
    public ArrayList<String> getStates() {
		return states;
	}



	public void setStates(ArrayList<String> states) {
		this.states = states;
	}



   
    public void add(String lit) {
        states.add (lit);
    }

   
    public String get(int index) {
        return (String) states.get(index);
    }

   
    public void remove(int index) {
        states.remove (index);
    }

   
    public boolean remove(String literal) {
        return states.remove (literal);
    }

    /**
     * adds theCnj to this conjunction and avoid duplication in states
     *  
     * @param theCnj The the cnj.
     */
    public void addConjunction(Conjunction theCnj) {

    	Iterator<String> it_cnj = theCnj.states.iterator();
    	while(it_cnj.hasNext()){
    		String cnj = it_cnj.next();
    		if(!this.states.contains(cnj)){
    			this.states.add(cnj);
    		}
    	}
    }
    
    public void removeConjunction(Conjunction theCnj) {

    	Iterator<String> it_cnj = theCnj.states.iterator();
    	while(it_cnj.hasNext()){
    		String cnj = it_cnj.next();
    		if(this.states.contains(cnj)){
    			this.states.remove(cnj);
    		}
    	}
    }

   
    public int size() {
        return states.size();
    }

    public boolean contains(Conjunction theSub) {
       
        Iterator<String> it_cnj = theSub.states.iterator();
        while (it_cnj.hasNext())
        {
        	String cnj = it_cnj.next();
            if (this.contains(cnj) == false)
            	return false;
        }
        return true;
    }

  
    public boolean contains(String ast) {
    	Iterator<String> it_st = this.states.iterator();
    	while(it_st.hasNext()){
    		String st = it_st.next();
    		if(st.equals(ast)){
    			return true;
    		}
    	}
    	return false;
    }

   
    public boolean Intersect(Conjunction theCnj) {
    	Iterator<String> it_cnj = theCnj.states.iterator();
        while (it_cnj.hasNext())
        {
        	String cnj = it_cnj.next();
            if (this.contains ( cnj))
            	return true;
        }
        return false;
    }

  
	public boolean equal(Conjunction theCnj) {
		if (states.size() != theCnj.size())
			return false;
		int len = states.size();
		for (int i = 0; i < len; i++) {
			if (this.contains(theCnj.get(i)) == false)
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
        	String st = states.get(i);
            s += st+"  ";
        }
        return s;
    }

}
