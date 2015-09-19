package graphplanner;

import pddl4j.PDDLObject;
import pddl4j.exp.type.TypeSet;

public class Object {
	
	private String objectName;
	private TypeSet type;
	
	
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public TypeSet getObjectType() {
		return type;
	}
	public void setObjectType(TypeSet objectType) {
		this.type = objectType;
	}
	
	public void initObject(PDDLObject domain){
		
	}
	
	public String toString(){
		String ret = new String();
		ret += " ("+type.toString()+") "+objectName;
		
		return ret;
	}
	
}
