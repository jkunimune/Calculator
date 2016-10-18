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

/**
 * A list of scalars. Vectors are commonly used to represent points and flows,
 * and have special dot and cross product operators.
 *
 * @author jkunimune
 */
public class Vector extends Expression {

	public Vector(List<Expression> e) {
		super(Operator.NULL, e);
	}
	
	
	
	public List<Expression> getComponents() {
		return args;
	}
	
	
	@Override
	public Expression simplified(Workspace heap) {
		return this;	//TODO: simplify components
	}
	
	
	@Override
	public Image toImage() {
		return null;	//TODO: vertCat it
	}
	
	
	@Override
	public String toString() {
		String output = "[";
		for (Expression arg: args)
			output += arg.toString()+", ";
		return output.substring(0, output.length()-2)+"]";
	}
	
	
	
	public static Vector concat(Expression... exps) {
		List<Expression> components = new ArrayList<Expression>();	// build a new Vector
		for (Expression exp: exps) {
			if (exp instanceof Vector)
				components.addAll(exp.args);	// by concatenating the inputs
			else
				components.add(exp);
		}
		return new Vector(components);
	}

}
