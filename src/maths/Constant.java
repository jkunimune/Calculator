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

import java.util.HashMap;

/**
 * An actual value, with no operators or variables attached. Represented as a
 * complex number with units.
 *
 * @author jkunimune
 */
public class Constant extends Expression {

	public static final Constant TAU = new Constant(2*Math.PI);
	public static final Constant PI = new Constant(Math.PI);
	public static final Constant E = new Constant(Math.E);
	
	public static final Constant ZERO = new Constant(0);
	public static final Constant ONE = new Constant(1);
	public static final Constant NEG_ONE = new Constant(-1);
	public static final Constant I = new Constant(0,1);
	
	
	
	private double real;
	private double imag;
	
	private HashMap<Dimension, Integer> dimensions;
	
	private int radix;
	
	
	
	public Constant(double r) {
		this(r, 0);
	}
	
	
	public Constant(double r, double i) {
		super(Operator.NULL);
		real = r;
		imag = i;
		dimensions = new HashMap<Dimension, Integer>();
		radix = 10;
	}
	
	
	
	public boolean matches(Constant that) {
		return this.dimensions.equals(that.dimensions);
	}
	
	
	@Override
	public Expression simplified(HashMap<String, Expression> h) {
		return this;	// Constants are already simplified
	}
	
	
	@Override
	public String toString() {
		if (radix == 10)
			return real+" + "+imag+"i";
		else
			return "blah blah blah, base not 10 stuff";
	}
	
	
	public Constant plus(Constant that) {
		return new Constant(this.real+that.real, this.imag+that.imag);
	}
	
	public Constant negative() {
		return new Constant(-real, -imag);
	}
	
	public Constant times(Constant that) {
		return new Constant(this.real*that.real - this.imag*that.imag,
				this.real*that.imag + this.imag*that.real);
	}
	
	public Constant recip() {
		final double r2 = real*real + imag*imag;
		return new Constant(real/r2, -imag/r2);
	}
	
	public Constant mod(Constant that) {
		return this.plus(this.times(that.recip()).floor().times(that).negative());
	}
	
	public Constant floor() {
		final double s = Math.floor(Math.hypot(real, imag))
				/Math.hypot(real, imag);
		return new Constant(s*real, s*imag);
	}
	
	public Constant sqrt() {
		return this.times(new Constant(0.5).ln()).exp();
	}
	
	public Constant exp() {
		final double mag = Math.exp(real);
		return new Constant(mag*Math.cos(imag), mag*Math.sin(imag));
	}
	
	public Constant ln() {
		return new Constant(Math.log(Math.hypot(real, imag)),
				Math.atan2(imag, real));
	}
	
	public Constant sin() {	// yeah, I'm defining sin in terms of sinh!
		return this.rot90(1).sinh();	// what of it?!
	}
	
	public Constant cos() {
		return this.rot90(1).cosh();
	}
	
	public Constant tan() {
		return this.rot90(1).tanh();
	}
	
	public Constant asin() {
		return this.asinh().rot90(-1);
	}
	
	public Constant acos() {
		return this.acosh().rot90(-1);
	}
	
	public Constant atan() {
		return this.atanh().rot90(-1);
	}
	
	public Constant sinh() {
		return this.exp().plus(this.negative().exp().negative())
				.times(new Constant(0.5));
	}
	
	public Constant cosh() {
		return this.exp().plus(this.negative().exp())
				.times(new Constant(0.5));
	}
	
	public Constant tanh() {
		return sinh().times(cosh().recip());
	}
	
	public Constant asinh() {
		return (this.plus(this.times(this).plus(ONE).sqrt())).ln();
	}
	
	public Constant acosh() {
		return (this.plus(this.times(this).plus(NEG_ONE).sqrt())).ln();
	}
	
	public Constant atanh() {
		final Constant half = new Constant(0.5);
		return ((this.plus(ONE).ln()).plus(this.plus(NEG_ONE).ln().negative()))
				.times(half);
	}
	
	public Constant abs() {
		return new Constant(Math.hypot(real, imag));
	}
	
	public Constant arg() {
		return new Constant(Math.atan2(imag, real));
	}
	
	
	public Constant rot90(int num) {	// multiply by i^num
		switch ((num+4)%4) {
		case 0:
			return this;
		case 1:
			return new Constant(-imag, real);
		case 2:
			return new Constant(-real, -imag);
		case 3:
			return new Constant(imag, -real);
		default:
			return null;
		}
	}

}
