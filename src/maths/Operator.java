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

/**
 * The built-in mathematical operations this program understands.
 *
 * @author jkunimune
 */
public enum Operator {

	NULL(0), ERROR(0), PARENTHESES(0),
	ADD(1), SUBTRACT(1),
	NEGATE(2), MULTIPLY(2), DIVIDE(2), MODULO(2), CROSS(2),
	POWER(3), TRANSVERSE(3), INVERSE(3),
	ROOT(5), LN(5), LOGBASE(5),SIN(5), COS(5), TAN(5), CSC(5), SEC(5),
	COT(5), ASIN(5), ACOS(5), ATAN(5), ACSC(5), ASEC(5), ACOT(5), SINH(5),
	COSH(5), TANH(5), CSCH(5), SECH(5), COTH(5), ASINH(5), ACOSH(5), ATANH(5),
	ACSCH(5), ASECH(5), ACOTH(5), ABSOLUTE(5), FUNC(5);
	
	
	
	private byte rank;
	
	private Operator(int r) {
		rank = (byte)r;
	}
	
	public byte getRank() {
		return rank;
	}

}
