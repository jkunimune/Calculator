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
import maths.auxiliary.Operator;
import util.ImgUtils;

/**
 * A list of scalars. Vectors are commonly used to represent points and flows,
 * and have special dot and cross product operators.
 *
 * @author jkunimune
 */
public class Vector extends Expression {

	private final Expression[] rows;
	private final boolean parenthetic; // this flag is mostly used by notation
	
	
	
	public Vector(boolean flag, List<Expression> comps) {
		rows = comps.toArray(new Expression[0]);
		parenthetic = false;
	}
	
	
	public Vector(Expression... comps) {
		rows = comps;
		parenthetic = false;
	}
	
	
	
	public Vector(boolean flag, Expression... comps) {
		rows = comps;
		parenthetic = flag;
	}


	public Expression[] getComponents() {
		return rows;
	}
	
	
	public boolean getParenthetic() {
		return parenthetic;
	}
	
	
	public Expression get(int index) {
		return rows[index];
	}
	
	
	@Override
	public int[] shape() {
		final int[] output = {rows.length, 1};
		return output;
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		return rows[i];
	}
	
	
	@Override
	public List<String> getInputs(Workspace heap) {
		return super.getInputsAll(rows, heap);
	}
	
	
	@Override
	public Expression replaced(String[] oldStrs, String[] newStrs) {
		return new Vector(super.replaceAll(rows, oldStrs, newStrs));
	}
	
	
	@Override
	public Vector simplified(Workspace heap) {
		return new Vector(super.simplifyAll(rows, heap));
	}
	
	
	@Override
	public Image toImage() {
		Image[] argImgs = new Image[rows.length];
		for (int i = 0; i < rows.length; i ++)
			argImgs[i] = rows[i].toImage();
		return ImgUtils.bind(ImgUtils.vertCat(true, argImgs));
	}
	
	
	@Override
	public String toString() {
		String output = "[";
		for (Expression arg: rows)
			output += arg.toString()+", ";
		return output.substring(0, output.length()-2)+"]";
	}
	
	
	public Vector plus(Vector that) {
		if (this.shape()[0] != that.shape()[0])
			throw new ArithmeticException("Cannot sum a vector in "
					+this.shape()[0]+"-space with a vector in "
					+that.shape()[0]+"-space.");
		
		Expression[] newComps = new Expression[shape()[0]];
		for (int i = 0; i < shape()[0]; i ++)
			newComps[i] = new Operation(Operator.ADD,this.rows[i],that.rows[i])
					.simplified();
		return new Vector(newComps);
	}
	
	
	public Vector negative() {
		Expression[] newComps = new Expression[shape()[0]];
		for (int i = 0; i < rows.length; i ++)
			newComps[i] = new Operation(Operator.NEGATE, rows[i]).simplified();
		return new Vector(newComps);
	}
	
	
	public Expression dot(Vector that) {
		Expression sum = null;
		for (int i = 0; i < shape()[0]; i ++) {
			final Expression prod = new Operation(Operator.MULTIPLY,
					this.get(i), that.get(i));
			if (sum == null)
				sum = prod;
			else
				sum = new Operation(Operator.ADD, sum, prod);
		}
		return sum.simplified();
	}
	
	
	public Vector cross(Vector that) {
		if (this.shape()[0] == 3 && that.shape()[0] == 3) {
			Expression[] newComps = new Expression[shape()[0]];
			for (int i = 0; i < 3; i ++) {
				final Expression vxuy = new Operation(Operator.MULTIPLY,
						this.get((i+1)%3), that.get((i+2)%3));
				final Expression vyux = new Operation(Operator.MULTIPLY,
						this.get((i+2)%3), that.get((i+1)%3));
				newComps[i] = new Operation(Operator.SUBTRACT, vxuy, vyux)
						.simplified();
			}
			return new Vector(newComps);
		}
		else if (this.shape()[0] == 7 && that.shape()[0] == 7) {
			throw new ArithmeticException("GAHH! The seven dimensional cross-"
					+ "product! Who the heck is trying to take a seven "
					+ "dimensional cross product?! I never implemented that! "
					+ "You! Stop trying to take seven dimensional cross "
					+ "products and reevaluate your life choices!");
		}
		else {
			throw new ArithmeticException("Cross-products are only "
					+ "defined for vectors of length 3 and 7.");
		}
	}
	
	
	public Vector times(Constant c) {
		Expression[] newComps = new Expression[shape()[0]];
		for (int i = 0; i < newComps.length; i ++)
			newComps[i] = new Operation(Operator.MULTIPLY, rows[i], c)
					.simplified();
		return new Vector(newComps);
	}
	
	
	public Expression abs() {	// calculate the magnitude
		Expression sum = null;
		for (Expression comp: rows) {
			final Expression v_i2 = new Operation(Operator.POWER,
					comp, Constant.TWO);
			if (sum == null)
				sum = v_i2;
			else
				sum = new Operation(Operator.ADD, sum, v_i2);
		}
		return new Operation(Operator.ROOT, sum, Constant.TWO).simplified();
	}
	
	
	
	public static Vector concat(Expression... exps) {
		return concat(false, exps);
	}
	
	
	public static Vector concat(boolean parenthetic, Expression... exps) {
		List<Expression> components = new ArrayList<Expression>();	// build a new Vector
		for (Expression exp: exps) {
			if (exp instanceof Vector)
				components.addAll(Arrays.asList(((Vector) exp).rows));	// by concatenating the inputs
			else
				components.add(exp);
		}
		return new Vector(parenthetic, components);
	}

}
