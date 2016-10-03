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

/**
 * A class full of static methods that relate standard mathematical notation to
 * Expression data-structures
 *
 * @author jkunimune
 */
public class Notation {

	public static final Expression parseExpression(String input) {	// create an expression from a String
		int level = 0;
		for (int i = 0; i < input.length(); i ++) {
			if (isOpenP(input.charAt(i)))
				level ++;
			if (isCloseP(input.charAt(i)))
				level --;
		}
		for (int i = 0; i < level; i ++)	// add parentheses as necessary
			input = input+")";
		for (int i = 0; i > level; i --)
			input = "("+input;
		
		List<String> tokens = new ArrayList<String>();
		
		for (int i = 0; i < input.length(); i ++) {
			if (input.charAt(i) == ' ')	// spaces are ignored
				continue;
			else if (isSymbol(input.charAt(i)))	// symbols are taken one at a time
				tokens.add(input.substring(i,i+1));
			else if (isDigit(input.charAt(i))) {	// digits string together into numbers
				int j = i+1;
				while (j < input.length() && isDigit(input.charAt(j)))
					j ++;
				tokens.add(input.substring(i,j));
				i = j-1;
			}
			else {
				int j = i+1;	// a letter followed by letters, digits, or open parentheses
				while (j < input.length() && !isSymbol(input.charAt(j)))	// make variables
					j ++;
				if (j < input.length() && isOpenP(input.charAt(j)))
					j ++;
				tokens.add(input.substring(i,j));
				i = j-1;
			}
		}
		return parse(tokens);
	}
	
	
	private static final Expression parse(List<String> tokens) {
		final int n = tokens.size();
		
		if (n == 0)
			return Expression.NULL;
		if (n == 1) {
			if (isSymbol(tokens.get(0).charAt(0)))
				return new Expression(Operator.ERROR);
			else if (isDigit(tokens.get(0).charAt(0)))
				return new Constant(Double.parseDouble(tokens.get(0)));
			else
				return new Variable(tokens.get(0));
		}
		
		for (byte rank = 0; rank < 3; rank ++) {	// in order of operations
			int level = 0;
			boolean inParentheses = true;
			for (int i = n-1; i >= 0; i --) {
				final String s = tokens.get(i);
				
				if (i < n-1 && level == 0)
					inParentheses = false;
				if (isCloseP(s.charAt(s.length()-1)))
					level --;
				if (isOpenP(s.charAt(s.length()-1)))
					level ++;
				
				if (level == 0) {
					if (rank == 0) {	// arithmetic
						if (s.equals("+"))
							return new Expression(Operator.ADD,
									parse(tokens.subList(0, i)),
									parse(tokens.subList(i+1,n)));
						else if (s.equals("-"))
							if (i > 0 && !isOperator(tokens.get(i-1).charAt(0)))	// beware of negation pretending to be subtraction
								return new Expression(Operator.SUBTRACT,
										parse(tokens.subList(0, i)),
										parse(tokens.subList(i+1,n)));
					}
					if (rank == 1) {	// geometric
						if (s.equals("*"))
							return new Expression(Operator.MULTIPLY,
									parse(tokens.subList(0, i)),
									parse(tokens.subList(i+1,n)));
						else if (s.equals("/"))
							return new Expression(Operator.DIVIDE,
									parse(tokens.subList(0, i)),
									parse(tokens.subList(i+1,n)));
						else if (s.equals("\\"))
							return new Expression(Operator.DIVIDE,
									parse(tokens.subList(i+1,n)),
									parse(tokens.subList(0, i)));
						else if (s.equals("%"))
							return new Expression(Operator.MODULO,
									parse(tokens.subList(0, i)),
									parse(tokens.subList(i+1,n)));
					}
					if (rank == 2) {	// exponential
						if (s.equals("^"))
							return new Expression(Operator.POWER,
									parse(tokens.subList(0, i)),
									parse(tokens.subList(i+1,n)));
					}
				}
			}
			
			if (rank == 1 && tokens.get(0).equals("-"))	// the special negation operator
				return new Expression(Operator.NEGATE,
						parse(tokens.subList(1, tokens.size())));
			
			if (inParentheses) {	// brackets
				final String funcString = tokens.get(0).substring(0,
						tokens.get(0).length()-1);
				final Expression interior = parse(tokens.subList(1, n-1));
				
				if (funcString.isEmpty())
					return new Expression(Operator.PARENTHESES, interior);
				else if (funcString.equals("ln"))
					return new Expression(Operator.LN, interior);
				else if (funcString.equals("sin"))
					return new Expression(Operator.SIN, interior);
				else if (funcString.equals("cos"))
					return new Expression(Operator.COS, interior);
				else if (funcString.equals("tan"))
					return new Expression(Operator.TAN, interior);
				else if (funcString.equals("csc"))
					return new Expression(Operator.CSC, interior);
				else if (funcString.equals("sec"))
					return new Expression(Operator.SEC, interior);
				else if (funcString.equals("cot"))
					return new Expression(Operator.COT, interior);
				else if (funcString.equals("asin") ||
						funcString.equals("arcsin"))
					return new Expression(Operator.ASIN, interior);
				else if (funcString.equals("acos") ||
						funcString.equals("arccos"))
					return new Expression(Operator.ACOS, interior);
				else if (funcString.equals("atan") ||
						funcString.equals("arctan"))
					return new Expression(Operator.ATAN, interior);
				else if (funcString.equals("acsc") ||
						funcString.equals("arccsc"))
					return new Expression(Operator.ACSC, interior);
				else if (funcString.equals("asec") ||
						funcString.equals("arcsec"))
					return new Expression(Operator.ASEC, interior);
				else if (funcString.equals("acot") ||
						funcString.equals("arccot"))
					return new Expression(Operator.ACOT, interior);
				else if (funcString.equals("sinh"))
					return new Expression(Operator.SINH, interior);
				else if (funcString.equals("cosh"))
					return new Expression(Operator.COSH, interior);
				else if (funcString.equals("tanh"))
					return new Expression(Operator.TANH, interior);
				else if (funcString.equals("csch"))
					return new Expression(Operator.CSCH, interior);
				else if (funcString.equals("sech"))
					return new Expression(Operator.SECH, interior);
				else if (funcString.equals("coth"))
					return new Expression(Operator.COTH, interior);
				else if (funcString.equals("asinh") ||
						funcString.equals("arcsinh"))
					return new Expression(Operator.ASINH, interior);
				else if (funcString.equals("acosh") ||
						funcString.equals("arccosh"))
					return new Expression(Operator.ACOSH, interior);
				else if (funcString.equals("atanh") ||
						funcString.equals("arctanh"))
					return new Expression(Operator.ATANH, interior);
				else if (funcString.equals("acsch") ||
						funcString.equals("arccsch"))
					return new Expression(Operator.ACSCH, interior);
				else if (funcString.equals("asech") ||
						funcString.equals("arcsech"))
					return new Expression(Operator.ASECH, interior);
				else if (funcString.equals("acoth") ||
						funcString.equals("arccoth"))
					return new Expression(Operator.ACOTH, interior);
				else if (funcString.equals("abs") || funcString.equals("norm"))
					return new Expression(Operator.ABSOLUTE, interior);
				else if (funcString.equals("arg"))
					return new Expression(Operator.ARGUMENT, interior);
				else if (funcString.equals("sqrt"))
					return new Expression(Operator.ROOT,
							interior, Constant.TWO);
				else
					return new Expression(Operator.FUNCTION,
							new Variable(funcString), interior);
			}
		}
		
		throw new IllegalArgumentException("No operators detected in "+tokens);
	}
	
	
	private static final boolean isDigit(char c) {
		return c >= '.' && c <= '9' && c != '/';
	}
	
	
	private static final boolean isSymbol(char c) {
		return isOperator(c) || isOpenP(c) || isCloseP(c) || c == ' ';
	}
	
	
	private static final boolean isOperator(char c) {
		final char[] ops = {'+','-','*','/','\\','%','^'};
		for (char o: ops)
			if (c == o)
				return true;
		return false;
	}
	
	
	private static final boolean isOpenP(char c) {
		final char[] open = {'(','[','{'};
		for (char o: open)
			if (c == o)
				return true;
		return false;
	}
	
	
	private static final boolean isCloseP(char c) {
		final char[] close = {')',']','}'};
		for (char o: close)
			if (c == o)
				return true;
		return false;
	}

}
