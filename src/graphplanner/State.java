package graphplanner;

import java.util.ArrayList;

public class State {

	private String stateType;
	private String predicate;
	private ArrayList<String> parameter;
	
	public State() {
		super();
		// TODO Auto-generated constructor stub
		parameter = new ArrayList<String>();
	}
	
	public State(State astate) {
		super();
		// TODO Auto-generated constructor stub
		parameter = new ArrayList<String>(astate.getParameter());
		stateType = astate.getStateType();
		predicate = astate.getPredicate();
	}

	public String getStateType() {
		return stateType;
	}

	public void setStateType(String stateType) {
		this.stateType = stateType;
	}

	public String getPredicate() {
		return predicate;
	}
	
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	
	public ArrayList<String> getParameter() {
		return parameter;
	}
	
	public void setParameter(ArrayList<String> parameter) {
		this.parameter = parameter;
	}
	
	public void addParameter(String para){
		this.parameter.add(para);
	}
	
	public boolean equals(State ast){
		if(!this.getPredicate().equals(ast.getPredicate())) return false;
		int len1 = this.getParameter().size();
		int len2 = ast.getParameter().size();
		if(len1 != len2) return false;
		
		for(int i=0; i<len1; i++){
			if(!this.getParameter().get(i).equals(ast.getParameter().get(i))){
				return false;
			}
			
		}
		
		return true;
	}
}
