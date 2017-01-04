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
	private final List<Expression> args;
	
	
	
	public Function(String nm, Expression exp) {
		name = nm;
		args = new ArrayList<Expression>(1);
		args.add(exp);
	}
	
	
	public Function(String nm, List<Expression> expLst) {
		name = nm;
		args = expLst;
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
	
	
	public List<String> getArgs() {
		List<String> output = new ArrayList<String>();
		for (Expression arg: args)
			output.add(arg.toString());
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
	public Expression replaced(List<String> oldS, List<String> newS) {
		List<Expression> modArgs = new ArrayList<Expression>();
		for (Expression arg: args)
			modArgs.add(arg.replaced(oldS, newS));
		return new Function(name, modArgs);
	}
	
	
	@Override
	public Expression simplified(Workspace heap) {
		if (heap != null && heap.containsKey(name)) {
			if (heap.getArgs(name).size() != args.size())
				throw new ArithmeticException(name+" takes "+heap.getArgs(name).size()+" arguments!");
			
			List<String> oldArgs = heap.getArgs(name);
			List<String> modArgs = new ArrayList<String>(args.size());
			for (String oldArg: oldArgs)
				modArgs.add(name+"/"+oldArg);
			final Workspace localHeap = heap.localize(modArgs, args);
			
			return heap.get(name).replaced(oldArgs, modArgs)
					.simplified(localHeap);
		}
		else {
			return this;
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
