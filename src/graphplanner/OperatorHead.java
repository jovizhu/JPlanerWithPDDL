package graphplanner;

import java.util.*;
import java.util.Map.Entry;

public class OperatorHead {

	public String opName;

	// var list HashMap<var_name, type>
	public HashMap<String, String> varList;
	public HashMap<String, String> parList;

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public HashMap<String, String> getVarList() {
		return varList;
	}

	public void setVarList(HashMap<String, String> varList) {
		this.varList = varList;
	}

	public OperatorHead() {
		this.opName = null;
		this.varList = new HashMap<String, String>();
		this.parList = new HashMap<String, String>();
	}

	public OperatorHead(String name, HashMap<String, String> vl) {
		this.opName = name;
		this.varList = vl;
	}

	public Vector<String> getTypes() {
		Vector<String> varTypes = new Vector<String>();
		Iterator<Map.Entry<String, String>> it_var = varList.entrySet()
				.iterator();
		while (it_var.hasNext()) {
			Entry<String, String> pa = it_var.next();
			varTypes.addElement(pa.getValue());

		}
		return varTypes;
	}

	public Vector<String> getVars() {
		Vector<String> vars = new Vector<String>();
		Iterator<Map.Entry<String, String>> it_var = varList.entrySet()
				.iterator();
		while (it_var.hasNext()) {
			Entry<String, String> pa = it_var.next();
			vars.addElement(pa.getKey());

		}
		return vars;
	}

	public String toString() {
		return (opName + "\n\tvar list "+varList.toString()+"\n\tpara list"+parList.toString());
	}

} // end TOpHead
