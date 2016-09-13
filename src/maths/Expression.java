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

import javafx.scene.image.Image;

/**
 * A combination of mathematical symbols and notation that can evaluate to some
 * value. These objects handle most of the mathematical capabilities of
 * Math-Assist
 *
 * @author jkunimune
 */
public class Expression {

	public static final Expression parseExpression(String input) {	// create an expression from a String
		ArrayList<Expression> temp = new ArrayList<Expression>();
		temp.add(new Variable("four"));
		temp.add(new Variable("two"));
		return new Expression(Operator.ADD, temp);
	}
	
	
	
	private Operator opr;
	private List<Expression> args;
	
	
	
	protected Expression(Operator o) {
		this(o, new ArrayList<Expression>());
	}
	
	
	protected Expression(Operator o, List<Expression> r) {
		opr = o;
		args = r;
	}
	
	
	
	public Expression simplified() {
		// TODO
		return this;
	}
	
	
	public String toString() {
		String output = opr.toString();
		output += "(";
		for (Expression arg: args)
			output += arg.toString()+",";
		return output+")";
	}
	
	
	public int getInputDimensions() {
		// TODO
		return 0;
	}
	
	
	public int getOutputDimensions() {
		// TODO
		return 0;
	}


	public Image formatted() {
		// TODO Auto-generated method stub
		return null;
	}

}
