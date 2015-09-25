package graphplanner;

import java.util.ArrayList;
import java.util.Iterator;

public class Condition {
	
	private String statType;


	private String predicate;
	private ArrayList<String> parameter;
	
	public Condition() {
		super();
		// TODO Auto-generated constructor stub
		
		parameter = new ArrayList<String>();
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
	
	public void addParameter(String para){
		parameter.add(para);
	}

	public void setParameter(ArrayList<String> parameter) {
		this.parameter = parameter;
	}
	
	public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}
	
	public String toString(){
		String ret  = new String();
		ret += statType+" "+predicate+" (";
		Iterator<String> it_para = parameter.iterator();
		while(it_para.hasNext()){
			ret += it_para.next();
		}
		ret += ")  ";
		
		return ret;
	}
	
	
}
