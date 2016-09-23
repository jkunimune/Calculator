/**
 * MIT License
 *
 * Copyright (c) 2016 Justin Kunimune
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package maths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.image.Image;

/**
 * A combination of mathematical symbols and notation that can evaluate to some
 * value. These objects handle most of the mathematical capabilities of
 * Math-Assist
 *
 * @author jkunimune
 */
public class Expression {

	public static final Expression NULL = new Expression(Operator.NULL);	// used when an expression is blank
	public static final Expression ERROR = new Expression(Operator.ERROR);	// used when an expression cannot be read
	
	
	private final Operator opr;
	private final List<Expression> args;
	
	
	
	public Expression(Operator o) {
		this(o, new ArrayList<Expression>());
	}
	
	
	public Expression(Operator o, List<Expression> r) {
		opr = o;
		args = r;
	}
	
	
	public Expression(Operator o, Expression... r) {
		opr = o;
		args = Arrays.asList(r);
	}
	
	
	
	public Expression simplified(HashMap<String, Expression> heap) {
		List<Expression> simplArgs = new ArrayList<Expression>(args.size());
		boolean allConstant = true;
		for (Expression e: args) {	// look at each argument
			final Expression simple = e.simplified(heap);	// simplify it
			simplArgs.add(simple);		// and store the simpler value
			if (!(simple instanceof Constant))	// if it still contains unknowns
				allConstant = false;	// we lose our ability to evaluate
		}
		
		if (opr != Operator.NULL && allConstant)	// if possible,
			return evaluate(opr, simplArgs);	// evaluate this expression
		else
			return new Expression(opr, simplArgs);
	}
	
	
	public Image formatted() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String toString() {
		/*switch (opr) {
		case NULL:
			return "\u2205";
		case ERROR:
			return "error";
		case PARENTHESES:
			return "("+args.get(0)+")";
		case ADD:
			String out1 = "";
			for (Expression e: args)
				out1 += e.toString()+" + ";
			return out1.substring(0,out1.length()-3);
		case SUBTRACT:
			return args.get(0)+" - "+args.get(1);
		case NEGATE:
			return "-"+args.get(0);
		case MULTIPLY:
			String out2 = args.get(0).toString();
			for (int i = 1; i < args.size(); i ++) {
				if (args.get(i) instanceof Constant &&	// if there are two consecutive constants
						args.get(i-1) instanceof Constant)
					out2 += "*";						// use asterisk
				else									// for everything else
					out2 += " ";						// use space
				out2 += args.get(i);
			}
			return out2;
		case DIVIDE:
			return args.get(0)+"/"+args.get(1);
		case MODULO:
			return args.get(0)+"%"+args.get(1);
		case CROSS:
			return args.get(0)+" \u00d7 "+args.get(1);
		case POWER:
			return args.get(0)+"^"+args.get(1);
		case TRANSVERSE:
			return args.get(0)+"T";
		case INVERSE:
			return args.get(0)+"^(-1)";
		case ROOT:
			if (args.get(1).equals(Constant.TWO))
				return "\u221A("+args.get(0)+")";
			else
				return args.get(1)+"\u221A("+args.get(0)+")";
		case LN:
			return "ln("+args.get(0)+")";
		case LOGBASE:
			if (args.get(0).equals(Constant.TEN))
				return "log("+args.get(1)+")";
			else
				return "log_"+args.get(0)+"("+args.get(1)+")";
		case SIN:
			return "sin("+args.get(0)+")";
		case COS:
			return "cos("+args.get(0)+")";
		case TAN:
			return "tan("+args.get(0)+")";
		case CSC:
			return "csc("+args.get(0)+")";
		case SEC:
			return "sec("+args.get(0)+")";
		case COT:
			return "cot("+args.get(0)+")";
		case ASIN:
			return "arcsin("+args.get(0)+")";
		case ACOS:
			return "arccos("+args.get(0)+")";
		case ATAN:
			return "arctan("+args.get(0)+")";
		case ACSC:
			return "arccsc("+args.get(0)+")";
		case ASEC:
			return "arcsec("+args.get(0)+")";
		case ACOT:
			return "arccot("+args.get(0)+")";
		case SINH:
			return "sinh("+args.get(0)+")";
		case COSH:
			return "cosh("+args.get(0)+")";
		case TANH:
			return "tanh("+args.get(0)+")";
		case CSCH:
			return "csch("+args.get(0)+")";
		case SECH:
			return "sech("+args.get(0)+")";
		case COTH:
			return "coth("+args.get(0)+")";
		case ASINH:
			return "arcsinh("+args.get(0)+")";
		case ACOSH:
			return "arccosh("+args.get(0)+")";
		case ATANH:
			return "arctanh("+args.get(0)+")";
		case ACSCH:
			return "arccsch("+args.get(0)+")";
		case ASECH:
			return "arcsech("+args.get(0)+")";
		case ACOTH:
			return "arccoth("+args.get(0)+")";
		case ABSOLUTE:
			return "|"+args.get(0)+"|";
		case ARGUMENT:
			return "arg("+args.get(0)+")";
		case FUNCTION:
			return args.get(0)+"("+args.get(1)+")";
		default:
			throw new IllegalArgumentException(opr.toString());
		}*/
		String out = opr+"(";
		for (Expression arg: args)
			out += arg+",";
		return out+")";
	}
	
	
	
	private static Constant evaluate(Operator opr, List<Expression> expLst) {
		Constant[] args = new Constant[expLst.size()];	// start by converting
		for (int i = 0; i < expLst.size(); i ++)		// the arguments to
			args[i] = (Constant) expLst.get(i);			// constants
		
		switch (opr) {	// then call the appropriate method
		case NULL:
			throw new IllegalArgumentException(expLst.toString());
		case ERROR:
			return new Constant(Double.NaN);
		case ADD:
			return args[0].plus(args[1]);
		case SUBTRACT:
			return args[0].plus(args[1].negative());
		case NEGATE:
			return args[0].negative();
		case MULTIPLY:
			return args[0].times(args[1]);
		case DIVIDE:
			return args[0].times(args[1].recip());
		case MODULO:
			return args[0].mod(args[1]);
		case POWER:
			return args[0].ln().times(args[1]).exp();
		case ROOT:
			return args[0].ln().times(args[1].recip()).exp();
		case LN:
			System.out.println(args[0]);
			System.out.println(args[0].getClass());
			System.out.println(args[0].real+" "+args[0].imag);
			System.out.println(args[0].ln());
			System.out.println();
			return args[0].ln();
		case LOGBASE:
			return args[1].ln().times(args[0].ln().recip());
		case SIN:
			return args[0].sin();
		case COS:
			return args[0].cos();
		case TAN:
			return args[0].tan();
		case CSC:
			return args[0].sin().recip();
		case SEC:
			return args[0].sin().recip();
		case COT:
			return args[0].tan().recip();
		case ASIN:
			return args[0].asin();
		case ACOS:
			return args[0].acos();
		case ATAN:
			return args[0].atan();
		case ACSC:
			return args[0].recip().asin();
		case ASEC:
			return args[0].recip().acos();
		case ACOT:
			return args[0].recip().atan();
		case SINH:
			return args[0].sinh();
		case COSH:
			return args[0].cosh();
		case TANH:
			return args[0].tanh();
		case CSCH:
			return args[0].sinh().recip();
		case SECH:
			return args[0].cosh().recip();
		case COTH:
			return args[0].tanh().recip();
		case ASINH:
			return args[0].asinh();
		case ACOSH:
			return args[0].acosh();
		case ATANH:
			return args[0].atanh();
		case ACSCH:
			return args[0].recip().asinh();
		case ASECH:
			return args[0].recip().acosh();
		case ACOTH:
			return args[0].recip().atanh();
		case CROSS:
			throw new RuntimeException("not implemented");
		case ABSOLUTE:
			return args[0].abs();
		case ARGUMENT:
			return args[0].arg();
		case TRANSVERSE:
			throw new RuntimeException("not implemented");
		case INVERSE:
			throw new RuntimeException("not implemented");
		case FUNCTION:
			throw new RuntimeException("not implemented");
		case PARENTHESES:
			return args[0];
		default:
			throw new IllegalArgumentException(opr.toString());
		}
	}

}
