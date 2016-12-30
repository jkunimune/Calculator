/**
 * 
 */
package maths;

import java.util.List;

import gui.Workspace;
import javafx.scene.image.Image;
import util.ImgUtils;

/**
 * An operation where the operator is some simple function
 * 
 * @author jkunimune
 */
public class BuiltInFunction extends Expression {

	private static final String[] FUNCTIONS = {"sin","cos","tan","csc","sec",
			"cot","sinh","cosh","tanh","csch","sech","coth","asin","acos",
			"atan","acsc","asec","acot","asinh","acosh","atanh","acsch","asech",
			"acoth","arcsin","arccos","arctan","arccsc","arcsec","arccot",
			"arcsinh","arccosh","arctanh","arccsch","arcsech","arccoth",
			"re", "real", "im", "imag", "abs","arg"};
	
	
	private String name;
	private Expression arg;
	
	
	
	public static final boolean recognizes(String s) {
		for (String f: FUNCTIONS)
			if (f.equalsIgnoreCase(s))
				return true;
		return false;
	}
	
	
	
	public BuiltInFunction(String n, Expression x) {
		name = n;
		arg = x;
	}
	
	
	
	private String getCode() {	// get a code unique to each function
		String code = name.toLowerCase();
		if (code.length() == 6 && code.startsWith("arc"))
			return "a"+code.substring(3);
		else if (code.equals("real") || code.equals("imag"))
			return code.substring(2);
		else
			return code;
	}
	
	
	@Override
	public int[] shape() {
		return arg.get(0).shape();
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		return new BuiltInFunction(name, arg.getComponent(i, j));
	}
	
	
	@Override
	public List<String> getInputs(Workspace heap) {
		return arg.getInputs(heap);
	}
	
	
	@Override
	public Expression replaced(List<String> oldStrs, List<String> newStrs) {
		return new Function(name, arg.replaced(oldStrs, newStrs));
	}
	
	
	@Override
	public Expression simplified(Workspace heap) {
		final Expression simp = arg.simplified(heap);
		if (!(simp instanceof Constant))
			return new BuiltInFunction(name, simp);
		
		Constant x = (Constant) simp;
		final String code = getCode();
		if (code.equals("sin"))
			return x.sin();
		else if (code.equals("cos"))
			return x.cos();
		else if (code.equals("tan"))
			return x.sin().times(x.cos().recip());
		else if (code.equals("csc"))
			return x.sin().recip();
		else if (code.equals("sec"))
			return x.cos().recip();
		else if (code.equals("cot"))
			return x.cos().times(x.sin().recip());
		else if (code.equals("sinh"))
			return x.sinh();
		else if (code.equals("cosh"))
			return x.cosh();
		else if (code.equals("tanh"))
			return x.sinh().times(x.cosh().recip());
		else if (code.equals("csch"))
			return x.sinh().recip();
		else if (code.equals("sech"))
			return x.cosh().recip();
		else if (code.equals("coth"))
			return x.cosh().times(x.sinh().recip());
		else if (code.equals("asin"))
			return x.asin();
		else if (code.equals("acos"))
			return x.acos();
		else if (code.equals("atan"))
			return x.atan();
		else if (code.equals("acsc"))
			return x.recip().asin();
		else if (code.equals("asec"))
			return x.recip().acos();
		else if (code.equals("acot"))
			return x.recip().atan();
		else if (code.equals("asinh"))
			return x.asinh();
		else if (code.equals("acosh"))
			return x.acosh();
		else if (code.equals("atanh"))
			return x.atanh();
		else if (code.equals("acsch"))
			return x.recip().asinh();
		else if (code.equals("asech"))
			return x.recip().acosh();
		else if (code.equals("acoth"))
			return x.recip().atanh();
		else if (code.equals("re"))
			return x.re();
		else if (code.equals("im"))
			return x.im();
		else if (code.equals("abs"))
			return x.abs();
		else if (code.equals("arg"))
			return x.arg();
		else
			throw new IllegalArgumentException("Unrecognized func: "+code);
	}
	
	
	@Override
	public Image toImage() {
		return ImgUtils.call(name, arg.toImage());
	}
	
	
	@Override
	public String toString() {
		return name+"("+arg+")";
	}

}
