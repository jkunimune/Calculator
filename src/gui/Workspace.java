/**
 * 
 */
package gui;

import java.util.HashMap;

import maths.Expression;

/**
 * @author jkunimune
 *
 */
public class Workspace {

	HashMap<String, Expression> heap;
	
	
	
	public Workspace() {
		heap = new HashMap<String, Expression>();
	}
	
	
	
	public boolean hasVariable(String s) {
		return heap.containsKey(s);
	}
	
	
	public Expression get(String s) {
		return heap.get(s);
	}
	
	
	public void put(String s, Expression e) {
		heap.put(s, e);
	}
	
	
	public HashMap<String, Expression> getHash() {
		return heap;
	}

}
