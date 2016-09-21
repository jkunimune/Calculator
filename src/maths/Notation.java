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
			if (input.charAt(i) == ' ')
				continue;
			else if (isSymbol(input.charAt(i)) && input.charAt(i) != ' ')
				tokens.add(input.substring(i,i+1));
			else if (isDigit(input.charAt(i))) {
				int j = i+1;
				while (j < input.length() && isDigit(input.charAt(j)))
					j ++;
				tokens.add(input.substring(i,j));
				i = j-1;
			}
			else {
				int j = i+1;
				while (j < input.length() && !isSymbol(input.charAt(j)))
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
			for (int i = 0; i < n; i ++) {
				final String s = tokens.get(i);
				
				if (i > 0 && level == 0)
					inParentheses = false;
				if (isOpenP(s.charAt(0)))
					level ++;
				if (isCloseP(s.charAt(0)))
					level --;
				
				if (level == 0) {
					if (rank == 0) {	// arithmetic
						if (s.equals("+"))
							return new Expression(Operator.ADD,
									parse(tokens.subList(0, i)),
									parse(tokens.subList(i+1,n)));
						else if (s.equals("-"))
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
			
			if (inParentheses)	// brackets
				return new Expression(Operator.PARENTHESES,
						parse(tokens.subList(1, n-1)));
		}
		
		throw new IllegalArgumentException(tokens.toString());
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
