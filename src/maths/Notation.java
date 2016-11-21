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

	public static final Statement parseStatement(String input) {	// create an expression from a String
		return parse(tokenize(input));
	}
	
	
	public static final List<String> tokenize(String input) {
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
		return tokens;
	}
	
	
	private static final Statement parse(List<String> tokens) {
		final List<Expression> exps = new ArrayList<Expression>();
		final List<Character> oprs = new ArrayList<Character>();
		int level = 0;
		int lastOperator = 0;
		for (int i = 0; i < tokens.size(); i ++) {
			if (level == 0) {
				final String s = tokens.get(i);
				if (s.length() == 1 && isComparator(s.charAt(0))) {
					exps.add(parEx(tokens.subList(lastOperator, i)));
					oprs.add(s.charAt(0));
					lastOperator = i+1;
				}
			}
		}
		exps.add(parEx(tokens.subList(lastOperator, tokens.size())));
		if (oprs.isEmpty())
			return exps.get(0);
		else
			return new Comparison(exps, oprs);
	}
	
	
	private static final Expression parEx(List<String> tokens) {	// parse an Expression
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
		
		for (byte rank = 0; rank < 4; rank ++) {	// in order of operations
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
					if (rank == 0) {	// vectors
						if (s.equals(",")) {
							return Vector.concat(
									parEx(tokens.subList(0, i)),
									parEx(tokens.subList(i+1, n)));
						}
					}
					if (rank == 1) {	// arithmetic
						if (s.equals("+"))
							return new Expression(Operator.ADD,
									parEx(tokens.subList(0, i)),
									parEx(tokens.subList(i+1,n)));
						else if (s.equals("-"))
							if (i > 0 && !isOperator(tokens.get(i-1).charAt(0)))	// beware of negation pretending to be subtraction
								return new Expression(Operator.SUBTRACT,
										parEx(tokens.subList(0, i)),
										parEx(tokens.subList(i+1,n)));
					}
					if (rank == 2) {	// geometric
						if (s.equals("*") || s.equals("\u2022"))
							return new Expression(Operator.MULTIPLY,
									parEx(tokens.subList(0, i)),
									parEx(tokens.subList(i+1,n)));
						else if (s.equals("\u00D7"))
							return new Expression(Operator.CROSS,
									parEx(tokens.subList(0, i)),
									parEx(tokens.subList(i+1,n)));
						else if (s.equals("/"))
							return new Expression(Operator.DIVIDE,
									parEx(tokens.subList(0, i)),
									parEx(tokens.subList(i+1,n)));
						else if (s.equals("\\"))
							return new Expression(Operator.DIVIDE,
									parEx(tokens.subList(i+1,n)),
									parEx(tokens.subList(0, i)));
						else if (s.equals("%"))
							return new Expression(Operator.MODULO,
									parEx(tokens.subList(0, i)),
									parEx(tokens.subList(i+1,n)));
						else if (i > 0 && !isOperator(tokens.get(i-1).charAt(0))
								&& !isOperator(s.charAt(0)))
							return new Expression(Operator.MULTIPLY,
									parEx(tokens.subList(0, i)),
									parEx(tokens.subList(i, n)));
					}
					if (rank == 3) {	// exponential
						if (s.equals("^"))
							return new Expression(Operator.POWER,
									parEx(tokens.subList(0, i)),
									parEx(tokens.subList(i+1,n)));
					}
				}
			}
			
			if (rank == 1 && tokens.get(0).equals("-"))	// the special negation operator
				return new Expression(Operator.NEGATE,
						parEx(tokens.subList(1, tokens.size())));
			
			if (inParentheses) {	// brackets
				final String funcString = tokens.get(0).substring(0,
						tokens.get(0).length()-1);
				final Expression interior = parEx(tokens.subList(1, n-1));
				
				if (funcString.isEmpty()) {
					if (interior instanceof Vector)	// vectors ignore parentheses
						return interior;
					else
						return new Expression(Operator.PARENTHESES, interior);
				}
				else if (funcString.equals("ln"))
					return new Expression(Operator.LN, interior);
				else if (funcString.equals("sqrt"))
					return new Expression(Operator.ROOT,
							interior, Constant.TWO);
				else if (BuiltInFunction.recognizes(funcString))
					return new BuiltInFunction(funcString, interior);
				else {
					if (interior instanceof Vector)
						return new Function(funcString,
								((Vector) interior).getComponents());
					else
						return new Function(funcString, interior);
				}
			}
		}
		
		throw new IllegalArgumentException("No operators detected in "+tokens);
	}
	
	
	private static final boolean isDigit(char c) {
		return c >= '.' && c <= '9' && c != '/';
	}
	
	
	private static final boolean isSymbol(char c) {
		return isComparator(c) || isOperator(c) || isOpenP(c) || isCloseP(c) ||
				c == ' ';
	}
	
	
	private static final boolean isComparator(char c) {
		final char[] comp = {'=','\u2260','<','\u2264','>','\u2265'};
		for (char o: comp)
			if (c == o)
				return true;
		return false;
	}
	
	
	private static final boolean isOperator(char c) {
		final char[] ops = {'+','-','*','\u2022','\u00D7','/','\\','%','^',','};
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
