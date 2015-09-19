package graphplanner;

import java.util.*;
import java.util.Map.Entry;

public final class G {

	public static final int MaxPara = 10;

	public static int MaxLevel = 10;

	// ------------------------------------------------------------------------
	// --- static methods ---
	// ------------------------------------------------------------------------
	/**
	 * uses the u unifier to unify each string int the vector v.
	 * 
	 * @param v
	 *            vector of strings.
	 * @param u
	 *            The unifier.
	 * @return The unified string in which each vector element is separated with
	 *         "&".
	 */
	public static String substitute(Vector v, Unifier u) {
		if (v == null)
			return null;
		String sub = new String();
		int len = v.size();
		for (int i = 0; i < len; i++) {
			String s = (String) v.elementAt(i);
			StringTokenizer st = new StringTokenizer(s, " (),\t\n\r\f", true);
			while (st.hasMoreTokens()) {
				String tok = st.nextToken();
				if (tok.startsWith("?") || tok.startsWith("@"))
					tok = u.get(tok);
				sub += tok;
			}
			sub += " & ";
		}
		return sub.substring(0, sub.length() - 3);
	}

	/**
	 * Unit testing method
	 * 
	 * @param args
	 *            The args array.
	 */
	public static void main(String[] args) {
		Vector v = new Vector();
		v.addElement(new String("On (?x, ?y)"));
		Unifier u = new Unifier("?x ?y ?z", "A B C");
		System.out.println(substitute(v, u));

		ArrayList<Condition> pre = new ArrayList<Condition>();
		Condition cond = new Condition();
		cond.setPredicate("ON");
		cond.addParameter("?A");
		cond.addParameter("?B");
		pre.add(cond);

		ArrayList<Unifier> unifiers = new ArrayList<Unifier>();
		Unifier uni = new Unifier();
		uni.getTable().put("?A", "a");
		uni.getTable().put("?B", "b");
		uni.getTable().put("?C", "c");

		unifiers.add(uni);
		Conjunction cnj = G.substitute(pre, uni);
		System.out.println(cnj.toString());
		
		OperatorHead oph = new OperatorHead();
		oph.opName="Test";
		oph.varList.put("?B", "B");
		oph.varList.put("?A", "A");
		OperatorHead new_oph = G.substitute(oph, uni);
		System.out.println(new_oph.toString());

	}

	public static Conjunction substitute(ArrayList<Condition> pre,
			Unifier unifier) {
		// TODO Auto-generated method stub

		Conjunction cnj = new Conjunction();

		Iterator<Condition> it_pre = pre.iterator();
		while (it_pre.hasNext()) {
			Condition con = (Condition) it_pre.next();
			ArrayList<String> pre_con_para_list = con.getParameter();
			Iterator<String> it_pre_con_para = pre_con_para_list.iterator();
			State st = new State();
			st.setPredicate(con.getPredicate());
			while (it_pre_con_para.hasNext()) {
				String pre_con_para = (String) it_pre_con_para.next();
				st.addParameter(unifier.get(pre_con_para));
			}
			cnj.add(st);
		}

		return cnj;
	}

	public static OperatorHead substitute(OperatorHead opHead, Unifier unifier) {
		// TODO Auto-generated method stub

		OperatorHead oph = new OperatorHead();
		oph.setOpName(opHead.opName);
		oph.varList = opHead.varList;
		
		Iterator<Entry<String, String>> it_head_var = opHead.varList.entrySet()
				.iterator();

		while (it_head_var.hasNext()) {
			Map.Entry<String, String> head_var = it_head_var.next();
			String var_name = (String) head_var.getKey();
			oph.parList.put(unifier.get(var_name), head_var.getValue());
			
		}

		return oph;
	}

} // end G
