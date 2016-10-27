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
import java.util.List;

import gui.Workspace;
import javafx.scene.image.Image;
import util.ImgUtils;

/**
 * A combination of mathematical symbols and notation that can evaluate to some
 * value. These objects handle most of the mathematical capabilities of
 * Math-Assist
 *
 * @author jkunimune
 */
public class Expression implements Statement {

	public static final Expression NULL = new Expression(Operator.NULL);	// used when an expression is blank
	public static final Expression ERROR = new Expression(Operator.ERROR);	// used when an expression cannot be read
	
	
	protected final Operator opr;
	protected final List<Expression> args;
	
	
	
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
	
	
	
	public int[] getDims() {
		return null;	//TODO: get dimensions recursively
	}
	
	
	public Expression simplified() {
		return this.simplified(null);
	}
	
	
	@Override
	public Expression simplified(Workspace heap) {
		final Expression[] sargs = new Expression[args.size()];
		for (int i = 0; i < args.size(); i ++)
			sargs[i] = args.get(i).simplified(heap);
		
		switch(opr) {
		case NULL:
			return Expression.NULL;
		case ERROR:
			return this;
		case PARENTHESES:
			return sargs[0];
		case ADD:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant)
				return ((Constant) sargs[0]).plus(((Constant) sargs[1]));
			if (sargs[0] instanceof Vector && sargs[1] instanceof Vector)
				return ((Vector) sargs[0]).plus(((Vector) sargs[1]));
			break;
		case SUBTRACT:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant)
				return ((Constant) sargs[0]).plus(((Constant) sargs[1]).negative());
			if (sargs[0] instanceof Vector && sargs[1] instanceof Vector)
				return ((Vector) sargs[0]).plus(((Vector) sargs[1]).negative());
			break;
		case NEGATE:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).negative();
			if (sargs[0] instanceof Vector)
				return ((Vector) sargs[0]).negative();
			break;
		case MULTIPLY:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant)
				return ((Constant) sargs[0]).times(((Constant) sargs[1]));
			if (sargs[0] instanceof Vector && sargs[1] instanceof Vector)
				return ((Vector) sargs[0]).dot(((Vector) sargs[1]));
			if (sargs[0] instanceof Vector && sargs[1] instanceof Constant)
				return ((Vector) sargs[0]).times(((Constant) sargs[1]));
			if (sargs[0] instanceof Constant && sargs[1] instanceof Vector)
				return ((Vector) sargs[1]).times(((Constant) sargs[0]));
			break;
		case DIVIDE:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant)
				return ((Constant)sargs[0]).times(((Constant)sargs[1]).recip());
			if (sargs[0] instanceof Vector && sargs[1] instanceof Constant)
				return ((Vector)sargs[0]).times(((Constant)sargs[1]).recip());
			break;
		case MODULO:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant)
				return ((Constant)sargs[0]).times(((Constant)sargs[1]).recip());
			break;
		case CROSS:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant)
				return ((Constant) sargs[0]).times(((Constant) sargs[1]));
			if (sargs[0] instanceof Vector && sargs[1] instanceof Vector)
				return ((Vector) sargs[0]).cross(((Vector) sargs[1]));
			if (sargs[0] instanceof Vector && sargs[1] instanceof Constant)
				return ((Vector) sargs[0]).times(((Constant) sargs[1]));
			if (sargs[0] instanceof Constant && sargs[1] instanceof Vector)
				return ((Vector) sargs[1]).times(((Constant) sargs[0]));
			break;
		case POWER:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant) {
				final Constant base = (Constant) sargs[0];
				final Constant power = (Constant) sargs[1];
				return base.ln().times(power).exp();
			}
			break;
		case TRANSVERSE:
			break;
		case INVERSE:
			break;
		case ROOT:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant) {
				final Constant base = (Constant) sargs[0];
				final Constant power = (Constant) sargs[1];
				return base.ln().times(power.recip()).exp();
			}
			break;
		case LN:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).ln();
			break;
		case LOGBASE:
			if (sargs[0] instanceof Constant && sargs[1] instanceof Constant) {
				final Constant base = (Constant) sargs[0];
				final Constant argument = (Constant) sargs[1];
				return argument.ln().times(base.ln().recip());
			}
			break;
		case SIN:	//XXX: This is ridiculous. THere are just too many of these. I need to make a comprehensive operator so I can Cast to constant and write their names more easily.
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).sin();
			break;
		case COS:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).cos();
			break;
		case TAN:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).tan();
			break;
		case CSC:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).sin().recip();
			break;
		case SEC:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).sin().recip();
			break;
		case COT:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).tan().recip();
			break;
		case ASIN:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).asin();
			break;
		case ACOS:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).acos();
			break;
		case ATAN:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).atan();
			break;
		case ACSC:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).recip().asin();
			break;
		case ASEC:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).recip().acos();
			break;
		case ACOT:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).recip().atan();
			break;
		case SINH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).sinh();
			break;
		case COSH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).cosh();
			break;
		case TANH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).tanh();
			break;
		case CSCH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).sinh().recip();
			break;
		case SECH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).cosh().recip();
			break;
		case COTH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).tanh().recip();
			break;
		case ASINH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).asinh();
			break;
		case ACOSH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).acosh();
			break;
		case ATANH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).atanh();
			break;
		case ACSCH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).recip().asinh();
			break;
		case ASECH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).recip().acosh();
			break;
		case ACOTH:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).recip().atanh();
			break;
		case ABSOLUTE:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).abs();
			if (sargs[0] instanceof Vector)
				return ((Vector) sargs[0]).abs();
			break;
		case ARGUMENT:
			if (sargs[0] instanceof Constant)
				return ((Constant) sargs[0]).arg();
			break;
		}
		
		return new Expression(opr, sargs);
	}
	
	
	@Override
	public Image toImage() {
		switch (opr) {
		case NULL:
			return ImgUtils.NULL;
		case ERROR:
			return ImgUtils.drawString("?");
		case PARENTHESES:
			return ImgUtils.wrap("(", args.get(0).toImage(), ")");
		case ADD:
			final List<Image> argImgs = new ArrayList<Image>();
			for (Expression arg: args)
				argImgs.add(arg.toImage());
			return ImgUtils.link(argImgs, " + ");
		case SUBTRACT:
			return ImgUtils.horzCat(args.get(0).toImage(),
					ImgUtils.drawString(" - "), args.get(1).toImage());
		case NEGATE:
			return ImgUtils.horzCat(ImgUtils.drawString("-"),
					args.get(0).toImage());
		case MULTIPLY:
			final List<Image> imgArgs = new ArrayList<Image>();
			for (Expression arg: args)
				imgArgs.add(arg.toImage());
			return ImgUtils.link(imgArgs, "\u2022");
		case DIVIDE:
			return ImgUtils.split(args.get(0).toImage(), args.get(1).toImage());
		case MODULO:
			return ImgUtils.horzCat(args.get(0).toImage(),
					ImgUtils.drawString("%"), args.get(1).toImage());
		case CROSS:
			return ImgUtils.horzCat(args.get(0).toImage(),
					ImgUtils.drawString("\u00d7"), args.get(1).toImage());
		case POWER:
			return ImgUtils.horzCat(args.get(0).toImage(),
					ImgUtils.superS(args.get(1).toImage()));
		case TRANSVERSE:
			return ImgUtils.horzCat(args.get(0).toImage(),
					ImgUtils.superS(ImgUtils.drawString("T")));
		case INVERSE:
			return ImgUtils.horzCat(args.get(0).toImage(),
					ImgUtils.superS(ImgUtils.drawString("-1")));
		case ROOT:
			Image out2 = ImgUtils.horzCat(ImgUtils.drawString("\u221A"),
					ImgUtils.overline(args.get(0).toImage()));
			if (!args.get(1).equals(Constant.TWO))
				out2 = ImgUtils.horzCat(ImgUtils.superS(args.get(1).toImage()), out2);
			return out2;
		case LN:
			return ImgUtils.call("ln", args.get(0).toImage());
		case LOGBASE:
			if (args.get(0).equals(Constant.TEN))
				return ImgUtils.call("log", args.get(1).toImage());
			else
				return ImgUtils.horzCat(ImgUtils.drawString("log"),
						ImgUtils.subS(args.get(0).toImage()),
						ImgUtils.wrap("(", args.get(0).toImage(), ")"));
		case SIN:
			return ImgUtils.call("sin", args.get(0).toImage());
		case COS:
			return ImgUtils.call("cos", args.get(0).toImage());
		case TAN:
			return ImgUtils.call("tan", args.get(0).toImage());
		case CSC:
			return ImgUtils.call("csc", args.get(0).toImage());
		case SEC:
			return ImgUtils.call("sec", args.get(0).toImage());
		case COT:
			return ImgUtils.call("cot", args.get(0).toImage());
		case ASIN:
			return ImgUtils.callInv("sin", args.get(0).toImage());
		case ACOS:
			return ImgUtils.callInv("cos", args.get(0).toImage());
		case ATAN:
			return ImgUtils.callInv("tan", args.get(0).toImage());
		case ACSC:
			return ImgUtils.callInv("csc", args.get(0).toImage());
		case ASEC:
			return ImgUtils.callInv("sec", args.get(0).toImage());
		case ACOT:
			return ImgUtils.callInv("cot", args.get(0).toImage());
		case SINH:
			return ImgUtils.call("sinh", args.get(0).toImage());
		case COSH:
			return ImgUtils.call("cosh", args.get(0).toImage());
		case TANH:
			return ImgUtils.call("tanh", args.get(0).toImage());
		case CSCH:
			return ImgUtils.call("csch", args.get(0).toImage());
		case SECH:
			return ImgUtils.call("sech", args.get(0).toImage());
		case COTH:
			return ImgUtils.call("coth", args.get(0).toImage());
		case ASINH:
			return ImgUtils.callInv("sinh", args.get(0).toImage());
		case ACOSH:
			return ImgUtils.callInv("cosh", args.get(0).toImage());
		case ATANH:
			return ImgUtils.callInv("tanh", args.get(0).toImage());
		case ACSCH:
			return ImgUtils.callInv("csch", args.get(0).toImage());
		case ASECH:
			return ImgUtils.callInv("sech", args.get(0).toImage());
		case ACOTH:
			return ImgUtils.callInv("coth", args.get(0).toImage());
		case ABSOLUTE:
			return ImgUtils.wrap("|", args.get(0).toImage(), "|");
		case ARGUMENT:
			return ImgUtils.call("arg", args.get(0).toImage());
		}
		return ImgUtils.NULL;
	}
	
	
	@Override
	public String toString() {
		switch (opr) {
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
				out2 += "\u2022"+args.get(i);
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
		}
		throw new IllegalArgumentException("Undefined operator: "+opr.toString());
	}

}
