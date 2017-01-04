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
import util.ImgUtils;

/**
 * A call to a user-defined function in the heap.
 *
 * @author jkunimune
 */
public class Function extends Expression {

	private final String name;
	private final Expression[] args;
	
	
	
	public Function(String nm, Expression...expressions) {
		name = nm;
		args = expressions;
	}
	
	
	public Function(String nm, List<Expression> expLst) {
		name = nm;
		args = expLst.toArray(new Expression[0]);
	}
	
	
	
	public boolean isStorable() {	// are all the inputs variables?
		for (Expression arg: args)
			if (! (arg instanceof Variable))
				return false;
		return true;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public String[] getArgs() {
		String[] output = new String[args.length];
		for (int i = 0; i < args.length; i ++)
			output[i] = args[i].toString();
		return output;
	}
	
	
	@Override
	public int[] shape() {
		return null; // there's no way to know without simplifying
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		return null;
	}


	@Override
	public List<String> getInputs(Workspace heap) {
		List<String> inputs = new ArrayList<String>(); //TODO this could be a Collection
		for (Expression arg: args)
			for (String newInput: arg.getInputs(heap))
				if (!inputs.contains(newInput))
					inputs.add(newInput);
		return inputs;
	}
	
	
	@Override
	public Expression replaced(String[] oldS, String[] newS) {
		List<Expression> modArgs = new ArrayList<Expression>();
		for (Expression arg: args)
			modArgs.add(arg.replaced(oldS, newS));
		return new Function(name, modArgs);
	}
	
	
	@Override
	public Expression simplified(Workspace heap) {
		if (heap != null && heap.containsKey(name)) {
			if (heap.getArgs(name).length != args.length)
				throw new ArithmeticException(name+" takes "+heap.getArgs(name).length+" arguments!");
			
			String[] oldArgs = heap.getArgs(name);
			String[] modArgs = new String[oldArgs.length];
			for (int i = 0; i < oldArgs.length; i ++)
				modArgs[i] = name+"/"+oldArgs[i];
			final Workspace localHeap = heap.localize(modArgs, args);
			
			return heap.get(name).replaced(oldArgs, modArgs)
					.simplified(localHeap);
		}
		else {
			return new Function(name, super.simplifyAll(args, heap));
		}
	}
	
	
	@Override
	public Image toImage() {
		List<Image> imgs = new ArrayList<Image>();
		for (Expression arg: args)
			imgs.add(arg.toImage());
		return ImgUtils.call(name, imgs, true);
	}
	
	
	@Override
	public String toString() {
		String output = name+"(";
		for (Expression arg: args)
			output += arg.toString()+", ";
		return output.substring(0, output.length()-2)+")";
	}

}
