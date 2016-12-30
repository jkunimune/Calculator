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
 * One or more sub-expressions combined with an Operator
 *
 * @author jkunimune
 */
public class Operation extends Expression {

	protected final Operator opr;
	protected final List<Expression> args;
	
	
	
	public Operation(Operator o) {
		this(o, new ArrayList<Expression>());
	}
	
	
	public Operation(Operator o, List<Expression> r) {
		opr = o;
		args = r;
	}
	
	
	public Operation(Operator o, Expression... r) {
		opr = o;
		args = Arrays.asList(r);
	}
	
	
	
	@Override
	public int[] shape() { // returns the length of the vector or the size of the array
		if (args.size() >= 1)
			return args.get(0).shape();
		else
			return null;
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		return this; //TODO later I'm going to come back and make this work for not-simplified vectors
	}
	
	
	@Override
	public List<String> getInputs(Workspace heap) { // returns all the variables on which this expression depends
		List<String> inputs = new ArrayList<String>(); //TODO this could be a Collection
		for (Expression arg: args)
			for (String newInput: arg.getInputs(heap))
				if (!inputs.contains(newInput))
					inputs.add(newInput);
		return inputs;
	}
	
	
	@Override
	public Expression replaced(List<String> oldS, List<String> newS) {
		List<Expression> modArgs = new ArrayList<Expression>();
		for (Expression arg: args)
			modArgs.add(arg.replaced(oldS, newS));
		return new Operation(opr, modArgs);
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
			if (sargs[0] instanceof Vector && sargs[1] instanceof Constant)	//TODO: This should work for all scalars - not just Constants
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
		}
		
		return new Operation(opr, sargs);
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
				out2 += "*"+args.get(i);
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
		}
		throw new IllegalArgumentException("Undefined operator: "+opr.toString());
	}

}
