package graphplanner;

import graphplanner.ActionLayer;
import graphplanner.Proposition;
import graphplanner.PropositionLayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import pddl4j.ErrorManager;
import pddl4j.PDDLObject;
import pddl4j.Parser;
import pddl4j.RequireKey;
import pddl4j.Source;
import pddl4j.ErrorManager.Message;
import pddl4j.exp.AndExp;
import pddl4j.exp.AtomicFormula;
import pddl4j.exp.Exp;
import pddl4j.exp.ExpID;
import pddl4j.exp.InitEl;
import pddl4j.exp.NotExp;
import pddl4j.exp.action.Action;
import pddl4j.exp.action.ActionDef;
import pddl4j.exp.action.ActionID;
import pddl4j.exp.term.Constant;
import pddl4j.exp.term.Term;
import pddl4j.exp.type.Type;
import pddl4j.exp.type.TypeSet;

public class GraphPlanner {

	public GraphPlanner() {
		super();
		// TODO Auto-generated constructor stub
		operatorSet = new OperatorSet();
		goal = new Conjunction();
		initials = new Conjunction();
		objects = new ArrayList<Object>();
		
	}

	ArrayList<Object> objects;
	Conjunction initials;
	Conjunction goal;
	OperatorSet operatorSet;
	PropositionLayer firstPropLayer;
	PropositionLayer lastPropLayer;
	
	int levels;
	int MaxLevel;

	public static void printInfo() {
		System.out.println("GraphPlanner factfile problemfile\n");
	}

	public static void main(String[] args) {

		if (args.length != 2) {
			printInfo();
			return;
		}

		File domainFile = new File(args[0]);
		File problemFile = new File(args[1]);

		// Gets the pddl compiler options
		Properties options = GraphPlanner.getParserOptions();
		Parser parser = new Parser(options);

		PDDLObject domain = null;

		try {
			domain = parser.parse(domainFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		PDDLObject problem = null;
		try {
			problem = parser.parse(problemFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		PDDLObject pb = null;
		if (domain != null && problem != null) {
			pb = parser.link(domain, problem);
		}

		// Gets the error manager of the pddl compiler
		ErrorManager mgr = parser.getErrorManager();
		// If the compilation produces errors we print it and stop
		if (mgr.contains(Message.ERROR)) {
			mgr.print(Message.ALL);
			return;
		}

		mgr.print(Message.WARNING);

		GraphPlanner graphplanner = new GraphPlanner();

		
		graphplanner.initObjects(pb);
		graphplanner.initOperators(pb);
		graphplanner.initGoals(pb);
		graphplanner.initInitials(pb);
		
		Iterator<Object> it_ob = graphplanner.objects.iterator();
		System.out.print("\nObject List:\n");
		while(it_ob.hasNext()){
			System.out.println(it_ob.next().toString());
		}
		System.out.print("\nGoal\n");
		System.out.println(graphplanner.goal.toString());
		System.out.print("\nInitial\n");
		System.out.println(graphplanner.initials.toString());
		System.out.print("\nOperator List\n");
		System.out.println(graphplanner.operatorSet.toString());
		System.out.println("\nParsing domain \"" + domain.getDomainName()
				+ "\" done successfully ...");
		System.out.println("Parsing problem \"" + problem.getProblemName()
				+ "\" done successfully ...\n");
		
		boolean done = graphplanner.createGraph();
        if (done){
        	System.out.println ("\nGraph Created Successfully");
        } else {
        	System.out.println ("\nGraph Creation Failed");
        }
        // this code is for debugging only
        // writes the planning graph to a text file
        try{
            FileWriter fw = new FileWriter ("output.gp");
            fw.write (graphplanner.toString(), 0, graphplanner.toString().length());
            fw.close();
        } catch (IOException e) {
        }
        // search for a valid plan
        boolean validPlan = false;
        if (done)
        {
            if (graphplanner.lastPropLayer.getPrevLayer().searchPlan(graphplanner.goal))	// plan is found for goal
            {
            	validPlan = true;
            }
            else
            System.out.println ("Error in searching for the plan");
        }
        // writing the plan
		if (validPlan) {
			Vector<graphplanner.Action> plan = graphplanner.getPlan();
			try {
				FileWriter fw = new FileWriter("output.pln");
				int len = plan.size();
				
				for (int i=0; i<len; i++) {
					String pstep = (String) plan.get(i).toString() + "\n";
					fw.write(pstep, 0, pstep.length());
					System.out.println(pstep);
				}
				fw.close();
				System.out.println("\nPlan Generated Successfully");
			} catch (IOException e) {
			}
		}

	}

	private static Properties getParserOptions() {
		Properties options = new Properties();
		options.put("source", Source.V3_0);
		options.put(RequireKey.STRIPS, true);
		options.put(RequireKey.TYPING, true);
		options.put(RequireKey.EQUALITY, true);
		options.put(RequireKey.NEGATIVE_PRECONDITIONS, false);
		options.put(RequireKey.DISJUNCTIVE_PRECONDITIONS, true);
		options.put(RequireKey.EXISTENTIAL_PRECONDITIONS, true);
		options.put(RequireKey.UNIVERSAL_PRECONDITIONS, true);
		options.put(RequireKey.CONDITIONAL_EFFECTS, true);
		return options;
	}

	
	public void initInitials(PDDLObject problem){
		
        List<InitEl> init = problem.getInit();
        for (InitEl el : init) {
            if (el.getExpID().equals(ExpID.ATOMIC_FORMULA)) {
                AtomicFormula predicate = (AtomicFormula) el;
                String st = new String();
                
                // for the state without parameters
				if (predicate.iterator().hasNext()) {
					st += predicate.getPredicate() + "(";
					Iterator<Term> iterator_init_para = predicate.iterator();
					while (iterator_init_para.hasNext()) {
						Term tm = iterator_init_para.next();
						String pa = tm.getImage();
						st += pa + ", ";
						// System.out.println(pa);
					}
					st = st.substring(0, st.length() - 2) + ")";
				}else{
					st += predicate.getPredicate();
				}
				System.out.println("Initial State:"+st);
                this.initials.add(st);
            } else {
            	System.err
				.println("Currently, we only support AND ATOMIC");
            }
        }
    }
	
	
	public void initGoals(PDDLObject problem){
        Exp goal = problem.getGoal();

		if (goal.getExpID() == ExpID.AND) {
			AndExp andExp = (AndExp) goal;
			Iterator<Exp> itr_goal = andExp.iterator();

			while (itr_goal.hasNext()) {
				Exp ep = itr_goal.next();
				switch (ep.getExpID()) {

				case ATOMIC_FORMULA:
					
					AtomicFormula ag = (AtomicFormula) ep;
					 String st = new String();
					if (ag.iterator().hasNext()) {
						st += ag.getPredicate() + "(";
						Iterator<Term> iterator_goal_para = ag.iterator();
						while (iterator_goal_para.hasNext()) {
							Term tm = iterator_goal_para.next();
							String pa = tm.getImage();
							st += pa + ", ";
							// System.out.println(pa);
						}
						st = st.substring(0, st.length() - 2) + ")";
					}else{
						st += ag.getPredicate();
					}
					System.out.println("Goal State:"+st);
					this.goal.add(st);
						break;
				case NOT:
					NotExp notExp = (NotExp) ep;
					Exp nexp = notExp.getExp();

					AtomicFormula np = (AtomicFormula) nexp;
					String nst = new String();
					
					if (np.iterator().hasNext()) {
						nst += "NOT-" + np.getPredicate() + "(";

						Iterator<Term> iterator_neff_para = np.iterator();
						while (iterator_neff_para.hasNext()) {
							Term ntm = iterator_neff_para.next();
							String npa = ntm.getImage();
							nst += npa + ", ";
							// System.out.println(npa);
						}
						nst = nst.substring(0, nst.length() - 2) + ")";
					}else{
						nst += "NOT-"+np.getPredicate();
					}
					System.out.println("Goal State:"+nst);
					this.goal.add(nst);
					break;
				default:
					System.err
							.println("Currently, we only support the NOT AND ATOMIC");
					break;
			}
			}
		}else if(goal.getExpID() == ExpID.ATOMIC_FORMULA){
			String st = new String();
			AtomicFormula ag = (AtomicFormula) goal;
			
			if (ag.iterator().hasNext()) {
				st += ag.getPredicate() + "(";
				Iterator<Term> iterator_goal_para = ag.iterator();
				while (iterator_goal_para.hasNext()) {
					Term tm = iterator_goal_para.next();
					String pa = tm.getImage();
					st += pa + ", ";
					// System.out.println(pa);
				}
				st = st.substring(0, st.length() - 2) + ")";
			}else{
				st += ag.getPredicate();
			}
			System.out.println("Goal State:"+st);
			this.goal.add(st);
		}else if(goal.getExpID() == ExpID.NOT){
			String nst = new String();
			AtomicFormula ag = (AtomicFormula) goal;
			if (ag.iterator().hasNext()) {
			nst += "NOT-"+ag.getPredicate()+"(";
			Iterator<Term> iterator_goal_para = ag.iterator();
			while (iterator_goal_para.hasNext()) {
				Term tm = iterator_goal_para.next();
				String npa = tm.getImage();
				nst += npa+", ";
				// System.out.println(pa);
			}
			nst =nst.substring(0,nst.length()-2)+")";
			}else{
				nst += "NOT-"+ag.getPredicate();
			}
			System.out.println("Goal State:"+nst);
			this.goal.add(nst);
		}
    }
	
	public void initObjects(PDDLObject domain){
		Iterator<Constant> it_constant =  domain.constantsIterator();
		while(it_constant.hasNext()){
			Constant ct = it_constant.next();
			Object ob = new Object();
			ob.setObjectName(ct.getImage());
			ob.setObjectType(ct.getTypeSet());
			System.out.print(ob.getObjectName());
			Iterator<Type> it_type = ct.getTypeSet().iterator();
			while(it_type.hasNext()){
				Type tp = (Type) it_type.next();
				System.out.print("\t"+tp.getImage());
			}
			this.objects.add(ob);
			System.out.println();
		}
	}
	
	public void initOperators(PDDLObject domain){

		Iterator<ActionDef> itertor_action = domain.actionsIterator();

		while (itertor_action.hasNext()) {
			ActionDef af = itertor_action.next();
			if (af.getActionID().equals(ActionID.ACTION)) {
				Action a = (Action) af;
				System.out.println(a.toString());
				Operator op = new Operator();
				op.operatorHead.setOpName(a.getName());

				// init the parameter
				List<Term> parameters = new ArrayList<Term>(a.getParameters());

				Iterator<Term> iterator_para = parameters.iterator();
				while (iterator_para.hasNext()) {
					Term tm = iterator_para.next();
					String para_name = tm.getImage();

					TypeSet types = tm.getTypeSet();
					Iterator<Type> iterator_tp = types.iterator();
					while (iterator_tp.hasNext()) {
						Type tp = iterator_tp.next();
						String para_type = tp.getImage();

						op.operatorHead.varList.put(para_name, para_type);
						System.out.println("op parameter " + para_name + "\t"
								+ para_type);
					}

				}

				// init the precondition
				ExpID expid = a.getPrecondition().getExpID();

				if (expid == ExpID.ATOMIC_FORMULA) {

					AtomicFormula pre = (AtomicFormula) a.getPrecondition();
					Condition st = new Condition();
					st.setStatType("ATOMIC_FORMULA");
					String pred = pre.getPredicate();
					st.setPredicate(pred);

					Iterator<Term> iterator_pre_para = pre.iterator();
					while (iterator_pre_para.hasNext()) {
						Term tm = iterator_pre_para.next();
						String pa = tm.getImage();
						st.addParameter(pa);
						System.out.println(pa);
					}
					op.precond.add(st);
				}else if (expid == ExpID.NOT) {

					NotExp nexp = (NotExp) a.getPrecondition();
					AtomicFormula np = (AtomicFormula) nexp.getExp();
					Condition st = new Condition();
					st.setStatType("NOT");
					String pred = np.getPredicate();
					st.setPredicate(pred);

					Iterator<Term> iterator_pre_para = np.iterator();
					while (iterator_pre_para.hasNext()) {
						Term tm = iterator_pre_para.next();
						String pa = tm.getImage();
						st.addParameter(pa);
						System.out.println(pa);
					}
					op.precond.add(st);
				} else {

					AndExp pre = (AndExp) a.getPrecondition();
					Iterator<Exp> iterator_pre = pre.iterator();
					while (iterator_pre.hasNext()) {
						Exp ep = iterator_pre.next();

						switch (ep.getExpID()) {

						case ATOMIC_FORMULA:
							AtomicFormula p = (AtomicFormula) ep;
							Condition st = new Condition();
							st.setStatType("ATOMIC_FORMULA");
							String pred = p.getPredicate();
							st.setPredicate(pred);

							Iterator<Term> iterator_pre_para = p.iterator();
							while (iterator_pre_para.hasNext()) {
								Term tm = iterator_pre_para.next();
								String pa = tm.getImage();
								st.addParameter(pa);
								// System.out.println(pa);
							}
							op.precond.add(st);
							break;
						case NOT:
							NotExp notExp = (NotExp) ep;
							Exp nexp = notExp.getExp();

							AtomicFormula np = (AtomicFormula) nexp;
							Condition nst = new Condition();
							nst.setStatType("NOT");
							String npred = np.getPredicate();
							nst.setPredicate(npred);

							Iterator<Term> iterator_npre_para = np.iterator();
							while (iterator_npre_para.hasNext()) {
								Term ntm = iterator_npre_para.next();
								String npa = ntm.getImage();
								nst.addParameter(npa);
								// System.out.println(npa);
							}
							op.precond.add(nst);
							break;
						default:
							System.err
									.println("Currently, we only support the NOT AND ATOMIC");
							break;
						}

					}
				}

				// init the effect
				expid = a.getEffect().getExpID();

				if (expid == ExpID.ATOMIC_FORMULA) {

					AtomicFormula eff = (AtomicFormula) a.getEffect();
					Condition st = new Condition();
					st.setStatType("ATOMIC_FORMULA");
					String pred = eff.getPredicate();
					st.setPredicate(pred);

					Iterator<Term> iterator_eff_para = eff.iterator();
					while (iterator_eff_para.hasNext()) {
						Term tm = iterator_eff_para.next();
						String pa = tm.getImage();
						st.addParameter(pa);
						System.out.println(pa);
					}
					op.addeff.add(st);
				}else if (expid == ExpID.NOT) {

					AtomicFormula eff = (AtomicFormula) a.getEffect();
					Condition st = new Condition();
					st.setStatType("NOT");
					String pred = eff.getPredicate();
					st.setPredicate(pred);

					Iterator<Term> iterator_eff_para = eff.iterator();
					while (iterator_eff_para.hasNext()) {
						Term tm = iterator_eff_para.next();
						String pa = tm.getImage();
						st.addParameter(pa);
						System.out.println(pa);
					}
					op.deleff.add(st);
				} else {

					AndExp eff = (AndExp) a.getEffect();
					Iterator<Exp> iterator_eff = eff.iterator();
					while (iterator_eff.hasNext()) {
						Exp ep = iterator_eff.next();

						switch (ep.getExpID()) {

						case ATOMIC_FORMULA:
							AtomicFormula p = (AtomicFormula) ep;
							Condition st = new Condition();
							st.setStatType("ATOMIC_FORMULA");
							String pred = p.getPredicate();
							st.setPredicate(pred);

							Iterator<Term> iterator_eff_para = p.iterator();
							while (iterator_eff_para.hasNext()) {
								Term tm = iterator_eff_para.next();
								String pa = tm.getImage();
								st.addParameter(pa);
								// System.out.println(pa);
							}
							op.addeff.add(st);
							break;
						case NOT:
							NotExp notExp = (NotExp) ep;
							Exp nexp = notExp.getExp();

							AtomicFormula np = (AtomicFormula) nexp;
							Condition nst = new Condition();
							nst.setStatType("NOT");
							String npred = np.getPredicate();
							nst.setPredicate(npred);

							Iterator<Term> iterator_neff_para = np.iterator();
							while (iterator_neff_para.hasNext()) {
								Term ntm = iterator_neff_para.next();
								String npa = ntm.getImage();
								nst.addParameter(npa);
								// System.out.println(npa);
							}
							op.deleff.add(nst);
							break;
						default:
							System.err
									.println("Currently, we only support the NOT AND ATOMIC");
							break;
						}

					}
				}
				
				//op.initParaConstants(objects);
				op.unifiers = op.initUnifiers(objects);
				this.operatorSet.operators.add(op);
				
			}
			
			
		}
	}
	
	public boolean createGraph(){

		firstPropLayer = new PropositionLayer();
		firstPropLayer.setInitLayer (initials);
        lastPropLayer = firstPropLayer;
        levels = 0;
        for (int i = 0; i < G.MaxLevel; i++)
        {
            levels++;
            // create Ai and Pi+1
            Vector<graphplanner.Action> acts = operatorSet.generateActions (lastPropLayer.getConjunction());
            if (acts.size() == 0)
            {
                // TODO: throw an exception
                System.out.println ("No applicable actions at level: " + levels);
                return false;
            }
            ActionLayer act_layer = new ActionLayer (lastPropLayer, acts );
            lastPropLayer.setNextLayer (act_layer);
            
            System.out.println("\nProposition Layer "+levels);
            System.out.println(lastPropLayer.toString());
            System.out.println("\nAction Layer");
            System.out.println(acts.toString());
            
            // point to Pi+1
            lastPropLayer = act_layer.getNextLayer();
            // test that all goals are reachable
            if (ReachableGoals())
            return true;
            // ToDo: test that graph levels off (Done)
            if (levelOff())
            {
                System.out.println ("Graph Levels Off at level" + levels);
                return false;
            }
        } // end for
        System.out.println ("Graph Creation Error: Max Level reached" + G.MaxLevel);
        return false;
    
	}
	
    private boolean ReachableGoals() {
        int len = goal.size();
        // all goals are reachbale
        for (int i = 0; i < len; i++)
        {
            if (lastPropLayer.getConjunction().contains (goal.get(i))== false)
            return false;
        }
        // no mutex relation
        for (int i = 0; i < len; i++)
        {
            Proposition p1 = lastPropLayer.getProposition (goal.get(i));
            for (int j = i + 1; j < len; j++)
            {
                Proposition p2 = lastPropLayer.getProposition (goal.get(j));
                if (p1.isMutex(p2))
                return false;
            }
        }
        return true;
    }
    
	private boolean levelOff() {
		if (levels < 2)
			return false;
		PropositionLayer p = lastPropLayer;
		ActionLayer act = lastPropLayer.getPrevLayer();
		System.out.println("lastProp size " + p.size()
				+ " vs act prelayer size " + act.getPrevLayer().size());

		if (p.equal(act.getPrevLayer()) == false)
			return false;
		
		p = act.getPrevLayer();
		
		Iterator<Map.Entry<String, Proposition>> it_prop = p.propositions.entrySet().iterator();
		while(it_prop.hasNext()){
			Proposition pro = it_prop.next().getValue();
			Proposition pa = p.getProposition(pro.getState());
			if(!pro.mutexEqual(pa)) return false;
		}
		
	
		if (act.equal(p.getPrevLayer()) == false)
			return false;

		return true;
	}
    
	public Vector<graphplanner.Action> getPlan() {
		Vector<graphplanner.Action> plan = new Vector<graphplanner.Action>();

		ActionLayer act = firstPropLayer.getNextLayer();
		while (act != null) {
			Vector<graphplanner.Action> applible_acts = act.getApplicableActions();
			System.out.println(applible_acts.toString());
			plan.addAll(applible_acts);
			PropositionLayer prop = act.getNextLayer();
			act = prop.getNextLayer();
		}
		return plan;
	}
    
}
