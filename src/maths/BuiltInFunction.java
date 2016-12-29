/**
 * 
 */
package maths;

import gui.Workspace;
import javafx.scene.image.Image;
import util.ImgUtils;

/**
 * @author jkunimune
 *
 */
public class BuiltInFunction extends Expression {

	private static final String[] FUNCTIONS = {"sin","cos","tan","csc","sec",
			"cot","sinh","cosh","tanh","csch","sech","coth","asin","acos",
			"atan","acsc","asec","acot","asinh","acosh","atanh","acsch","asech",
			"acoth","arcsin","arccos","arctan","arccsc","arcsec","arccot",
			"arcsinh","arccosh","arctanh","arccsch","arcsech","arccoth",
			"abs","arg"};
	
	
	private String name;
	
	
	
	public static final boolean recognizes(String s) {
		for (String f: FUNCTIONS)
			if (f.equals(s))
				return true;
		return false;
	}
	
	
	
	public BuiltInFunction(String n, Expression arg) {
		super(Operator.NULL, arg);
		name = n;
	}
	
	
	
	private String getCode() {	// get a code unique to each function
		if (name.length() == 6 && name.startsWith("arc"))
			return "a"+name.substring(3);
		else
			return name;
	}
	
	
	@Override
	public int[] shape() {
		return args.get(0).shape();
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		return new BuiltInFunction(name, args.get(0).getComponent(i, j));
	}
	
	
	@Override
	public Expression simplified(Workspace heap) {
		final Expression simp = args.get(0).simplified(heap);
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
		else if (code.equals("abs"))
			return x.abs();
		else if (code.equals("arg"))
			return x.arg();
		else
			throw new IllegalArgumentException("Unrecognized func: "+code);
	}
	
	
	@Override
	public Image toImage() {
		return ImgUtils.call(name, args.get(0).toImage());
	}
	
	
	@Override
	public String toString() {
		return name+"("+args.get(0)+")";
	}

}
