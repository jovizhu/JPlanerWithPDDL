package graphplanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import pddl4j.exp.type.Type;

public class Operator {



	public OperatorHead operatorHead;
	public HashMap<String, ArrayList<String>> paraConstants;

	//public ArrayList<String> allUnifiers;
	//public ArrayList<String> validUnifiers;
	
	
	public ArrayList<Condition> precond;

	public ArrayList<Condition> addeff;

	public ArrayList<Condition> deleff;

	public ArrayList<Unifier> unifiers;

	
	public Operator() {
		super();
		// TODO Auto-generated constructor stub
		operatorHead = new OperatorHead();
		precond = new ArrayList<Condition>();
		addeff = new ArrayList<Condition>();
		deleff = new ArrayList<Condition>();
		//validUnifiers = new ArrayList<String>();
		//allUnifiers = new ArrayList<String>();
		//paraConstants = new HashMap<String, ArrayList<String>>();
		
		unifiers = new ArrayList<Unifier>();
	}
/*	
	public ArrayList<String> getAllUnifiers() {
		return allUnifiers;
	}

	public void setAllUnifiers(ArrayList<String> allUnifiers) {
		this.allUnifiers = allUnifiers;
	}

	public ArrayList<String> getValidUnifiers() {
		return validUnifiers;
	}

	public void setValidUnifiers(ArrayList<String> validUnifiers) {
		this.validUnifiers = validUnifiers;
	}*/


	public ArrayList<Condition> getPrecond() {
		return precond;
	}

	public void setPrecond(ArrayList<Condition> precond) {
		this.precond = precond;
	}

	public ArrayList<Condition> getAdd() {
		return addeff;
	}

	public void setAdd(ArrayList<Condition> add) {
		this.addeff = add;
	}

	public ArrayList<Condition> getDel() {
		return deleff;
	}

	public void setDel(ArrayList<Condition> del) {
		this.deleff = del;
	}
	
	public void initParaConstants(ArrayList<Object> objects) {

		Iterator<Entry<String,String>> it_var = operatorHead.varList.entrySet().iterator();
		while (it_var.hasNext()) {
			Entry<String, String> var = it_var.next();
			String var_type = var.getValue();
			ArrayList<String> var_constant = new ArrayList<String>();
			Iterator<Object> it_object = objects.iterator();
			Unifier unifier = new Unifier();
			
			while (it_object.hasNext()) {
				Object ob = it_object.next();
				Iterator<Type> it_type = ob.getObjectType().iterator();
				while (it_type.hasNext()) {
					Type tp = it_type.next();
					if (var_type.equals(tp.getImage())) {
						var_constant.add(ob.getObjectName());
						unifier.getTable().put(var.getKey(), ob.getObjectName());
					}
				}

			}
			this.paraConstants.put(var.getKey(), var_constant);
		}
	}
	
	public ArrayList<Unifier> initUnifiers(ArrayList<Object> objects) {

		//ArrayList<Unifier> unifiers = new ArrayList<Unifier>();	
		Iterator<Entry<String, String>> it_var = operatorHead.varList
				.entrySet().iterator();
		
		ArrayList<Unifier>  old_unifiers = new ArrayList<Unifier>();
		ArrayList<Unifier>  current_unifiers = new ArrayList<Unifier>();
		
		while (it_var.hasNext()) {
			Entry<String, String> var = it_var.next();
			String var_type = var.getValue();
			String var_name = var.getKey();
			Iterator<Object> it_object = objects.iterator();
			
			
			while (it_object.hasNext()) {
				Object ob = it_object.next();
				Iterator<Type> it_type = ob.getObjectType().iterator();
				while (it_type.hasNext()) {
					Type tp = it_type.next();
					if (var_type.equals(tp.getImage())) {
						String ob_name = ob.getObjectName();
						
						if (old_unifiers.size() > 0) {
							Iterator<Unifier> it_old__unifier = old_unifiers
									.iterator();
							while (it_old__unifier.hasNext()) {
								Unifier un = new Unifier((Unifier) it_old__unifier.next());
								if (!un.getTable().containsValue(ob_name)) {
									un.add(var_name, ob_name);
									System.out
											.println(var_name + " " + ob_name);
									current_unifiers.add(un);
								}
							}
						} else {
							Unifier un = new Unifier();
							un.add(var_name, ob_name);
							System.out.println(var_name+" "+ob_name);
							current_unifiers.add(un);
						}
					}
				}

			}
			
			old_unifiers.clear();
			Iterator<Unifier> it_unifier = current_unifiers.iterator();
			while(it_unifier.hasNext()){
				Unifier un = (Unifier)it_unifier.next();
				old_unifiers.add(un);
			}
			
			current_unifiers.clear();

		}
		return old_unifiers;
	}
	
	 public Vector<Action> generateActions(Conjunction thePre) {
		 
	        Vector<Action> actions = new Vector<graphplanner.Action>();
	        
	        // note: there must be at least one variable
	        // or there is no pre, add, del == no operator
	        int len = unifiers.size();
	        for (int i = 0; i < len; i++)
	        {
	            Unifier un = (Unifier) unifiers.get(i);
	            //System.out.println(un.toString());
	            Conjunction apre = G.substitute(precond, un);
	           
	            // apre may be null
	            if (thePre.contains(apre))	// pre-condition is satisfied
	            {
	                // create an action
	            	Conjunction adel = G.substitute (deleff, un);
	            	Conjunction aadd = G.substitute (addeff, un);
	                OperatorHead ahead = G.substitute (operatorHead, un);
	                // add it to actions vector
	                actions.addElement (new Action (ahead, apre, aadd, adel));
	            }
	        }
	        return actions;
	    }

	 public String toString(){
		 String ret  = new String();
		 ret += operatorHead.toString();
		 
		 ret += "\n\tUnifiers\t";
		 Iterator<Unifier> it_un = unifiers.iterator();
		 while(it_un.hasNext()){
			 ret += "  [";
			 Unifier un = (Unifier)it_un.next();
			 Iterator<Map.Entry<String, String>> it_un_table_entry = un.getTable().entrySet().iterator();
			 while(it_un_table_entry.hasNext()){
				 Map.Entry<String, String> un_table_entry = it_un_table_entry.next();
				 ret += "("+un_table_entry.getKey()+") "+un_table_entry.getValue()+"  ";
			 }
			 ret += "]  ";
		 }
		 
		 ret += "\n\tPreConditon\t";
		 Iterator<Condition> it_pre = precond.iterator();
		 while(it_pre.hasNext()){
			 Condition cond = (Condition)it_pre.next();
			 ret += "  "+cond.toString();
		 }
		 
		 ret += "\n\tAdd Effect\t";
		 Iterator<Condition> it_addeff = addeff.iterator();
		 while(it_addeff.hasNext()){
			 Condition cond = (Condition)it_addeff.next();
			 ret += "  "+cond.toString();
		 }
		 
		 ret += "\n\tDel Effect\t";
		 Iterator<Condition> it_deleff = deleff.iterator();
		 while(it_deleff.hasNext()){
			 Condition cond = (Condition)it_deleff.next();
			 ret += "  "+cond.toString();
		 }
		 
		 ret += "\n";
		 return ret;
	 }
}
