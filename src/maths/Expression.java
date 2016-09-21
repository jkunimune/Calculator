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
		
		if (allConstant)	// if possible,
			return evaluate(opr, simplArgs);	// evaluate this expression
		else
			return new Expression(opr, simplArgs);
	}
	
	
	public Image formatted() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String toString() {
		String output = opr.toString();
		output += "(";
		for (Expression arg: args)
			output += arg.toString()+",";
		return output+")";
	}
	
	
	
	private static Constant evaluate(Operator opr, List<Expression> expLst) {
		Constant[] args = new Constant[expLst.size()];	// start by converting
		for (int i = 0; i < expLst.size(); i ++)		// the arguments to
			args[i] = (Constant) expLst.get(i);			// constants
		
		switch (opr) {	// then call the appropriate method
		case NULL:
			throw new IllegalArgumentException("Please override evaluate");
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
			return args[0].ln();
		case LOGBASE:
			return args[1].ln().times(args[0].ln().recip());
		case SIN:
			return args[0].sin();
		case COS:
			return args[0].cos();
		case TAN:
			return args[0].sin();
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
		case DOT:
			throw new RuntimeException("not implemented");
		case ABSOLUTE:
			return args[0].abs();
		case TRANSVERSE:
			throw new RuntimeException("not implemented");
		case INVERSE:
			throw new RuntimeException("not implemented");
		case DETERMINANT:
			throw new RuntimeException("not implemented");
		case FUNC:
			throw new RuntimeException("not implemented");
		case PARENTHESES:
			return args[0];
		default:
			throw new IllegalArgumentException(opr.toString());
		}
	}

}
