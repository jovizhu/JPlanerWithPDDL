package graphplanner;

import java.util.*;

/**
 * Class Unifier.
 * is a hashtable in the format
 * -------------------------- 
 *   Key      |  value
 * --------------------------
 *   ?x	  |  A
 *   ?y	  |  B
 *   ?z       |  C
 * --------------------------
 */
public class Unifier {

    private HashMap<String, String> table;

    public Unifier(Unifier unifier) {
        table = new HashMap<String, String>();
        Iterator<Map.Entry<String, String>> it_table = unifier.table.entrySet().iterator();
        while(it_table.hasNext()){
        	Map.Entry<String, String> item = it_table.next();
        	this.table.put(item.getKey(), item.getValue());
        }
    }
    
    public Unifier() {
        table = new HashMap<String, String>();

    }
    
    public HashMap<String,String> getTable(){
    	return table;
    }

    /**
     * Creates a new instance of Unifier.
     *  
     * @param varList a string represented a list of variables separated by spaces or ",".
     * @param valList a string represented a list of types separated by spaces or ","
     */
    public Unifier(String varList, String valList) {
        StringTokenizer st1 = new StringTokenizer (varList, " (),\t\n\r\f" , false);
        StringTokenizer st2 = new StringTokenizer (valList, " (),\t\n\r\f" , false);
        if (st1.countTokens() != st2.countTokens())
        {
            // thror exception and exit
        }
        table = new HashMap();
        while (st1.hasMoreTokens())
        {
            table.put (st1.nextToken(), st2.nextToken());
        }
    }

    /**
     * Creates a new instance of Unifier.
     *  
     * @param varList The var list.
     * @param valList The val list.
     */
    public Unifier(Vector varList, Vector valList) {
        if (varList.size()!= valList.size())
        {
            // TODO: throw RuntimeException and exit
        }
        table = new HashMap();
        int len = varList.size();
        for (int i = 0; i < len ; i++)
        table.put ((String) varList.elementAt(i), (String) valList.elementAt(i));
    }

    /**
     * Creates a new instance of Unifier.
     *  
     * @param varList The var list.
     * @param valList The val list.
     */
    public Unifier(Vector<String> varList, String valList) {
        StringTokenizer st2 = new StringTokenizer (valList, " (),\t\n\r\f" , false);
        int len = varList.size();
        if ( len != st2.countTokens())
        {
            // TODO: throw RuntimeException and exit
        }
        table = new HashMap<String, String>();
        for (int i = 0; i < len; i++)
        {
            table.put ((String) varList.elementAt(i), st2.nextToken());
        }
    }


    // ------------------------------------------------------------------------
    // --- method                                                           ---
    // ------------------------------------------------------------------------
    /**
     * get a unifier for the passed variable.
     */
    String get(String var) {
        if (var.startsWith ("@"))	// even numbers will start with @
        return var.substring(1);
        String val = (String) table.get(var);
        return val;
    }

    public void add(String var, String constant){
    	this.table.put(var, constant);
    }

    public String toString(){
    	String ret = new String();
         Iterator<Map.Entry<String, String>> it_table = this.table.entrySet().iterator();
         while(it_table.hasNext()){
         	Map.Entry<String, String> item = it_table.next();
         	ret +="["+"("+item.getKey()+") "+ item.getValue() +"]  ";
         }
         return ret;
    	
    }
    // ------------------------------------------------------------------------
    // --- static method                                                    ---
    // ------------------------------------------------------------------------
    /**
     * Unit testing method
     *  
     * @param args The args array.
     */
    public static void main(String[] args) {
        String var = new String ("?x, ?y, ?z");
        String val = new String ("A B C");
        Unifier u = new Unifier (var, val);
        System.out.println (u.get("?y"));
        System.out.println (u.get("@Table"));
    }

} // end Unifier
