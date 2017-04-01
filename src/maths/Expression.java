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
import java.util.List;

import gui.Workspace;
import javafx.scene.image.Image;
import maths.auxiliary.Operator;

/**
 * A combination of mathematical symbols and notation that can evaluate to some
 * value. These objects handle most of the mathematical capabilities of
 * Math-Assist
 *
 * @author jkunimune
 */
public abstract class Expression implements Statement {

	public static final Expression NULL = new Operation(Operator.NULL);	// used when an expression is blank
	public static final Expression ERROR = new Operation(Operator.ERROR);	// used when an expression cannot be read
	
	
	
	public abstract int[] shape(); // returns the length of the vector or the size of the array
	
	
	public Expression[] getAll(int n) { // get the first n components of this
		Expression[] output = new Expression[n];
		for (int i = 0; i < n; i ++)
			output[i] = this.get(i);
		return output;
	}
	
	
	public Expression get(int i) {
		int[] shape = this.shape();
		if (i < 0 || i >= shape[0]*shape[1])
			throw new ArithmeticException(this+" doesn't have a "+i+"th component");
		return this.getComponent(i/shape[0], i%shape[1]);
	}
	
	
	public Expression get(int i, int j) {
		int[] shape = this.shape();
		if (i < 0 || i >= shape[0] || j < 0 || j >= shape[1])
			throw new ArithmeticException(this+" doesn't have a "+i+" "+j+" element");
		return this.getComponent(i, j);
	}
	
	
	protected static Expression[] getComponentAll(Expression[] args,
			int i, int j) {
		Expression[] output = new Expression[args.length];
		for (int k = 0; k < args.length; k ++)
			output[k] = args[k].getComponent(i, j);
		return output;
	}
	
	
	protected abstract Expression getComponent(int i, int j);
	
	
	protected static List<String> getInputsAll(Expression[] args, Workspace heap) {
		List<String> output = new ArrayList<String>();
		for (Expression arg: args)
			for (String s: arg.getInputs(heap))
				if (!output.contains(s))
					output.add(s);
		return output;
	}
	
	
	public abstract List<String> getInputs(Workspace heap); // returns all the variables on which this expression depends
	
	
	protected static Expression[] replaceAll(Expression[] args,
			String[] oldStrs, String[] newStrs) { //XXX: I wonder if these should be arrays instead of lists
		Expression[] output = new Expression[args.length];
		for (int k = 0; k < args.length; k ++)
			output[k] = args[k].replaced(oldStrs, newStrs);
		return output;
	}
	
	
	public abstract Expression replaced(String[] oldArgs, String[] modArgs); // replace all instances of oldStrs[i] with nemStrs[i]
	
	
	public Expression simplified() throws ArithmeticException {
		return this.simplified(null);
	}
	
	
	protected static Expression[] simplifyAll(Expression[] args, Workspace heap) {
		Expression[] output = new Expression[args.length];
		for (int i = 0; i < args.length; i ++)
			output[i] = args[i].simplified(heap);
		return output;
	}
	
	
	@Override
	public abstract Expression simplified(Workspace heap) throws ArithmeticException;
	
	
	@Override
	public abstract Image toImage();
	
	
	@Override
	public abstract String toString();

}
