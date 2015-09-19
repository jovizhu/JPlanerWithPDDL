package graphplanner;

import java.util.ArrayList;
import java.util.Vector;


public class OperatorSet {


	ArrayList<Operator> operators;
	
	public OperatorSet() {
		super();
		// TODO Auto-generated constructor stub
		operators = new ArrayList<Operator>();
	}

	public Vector<graphplanner.Action> generateActions(Conjunction thePre) {
        Vector<graphplanner.Action> actions = new Vector<graphplanner.Action>();
        int len = operators.size();
        for (int i = 0; i < len; i++)
        {
            Vector<graphplanner.Action> temp = this.operators.get(i).generateActions (thePre);
            if ( temp.size() > 0)
              actions.addAll (temp);
        }
        return actions;
    }
	
	public String toString(){
		String ret = new String();
        int len = operators.size();
        for (int i = 0; i < len; i++)
        {
           ret +=  this.operators.get(i).toString();
 
        }
        ret +="\n";
        return ret;
	}



}
