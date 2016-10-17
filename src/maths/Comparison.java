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
import java.util.List;

import javafx.scene.image.Image;
import util.ImgUtils;

/**
 * An equality or inequality of Expressions.
 *
 * @author jkunimune
 */
public class Comparison implements Statement {

	private final List<Expression> expressions;
	private final List<Character> operators;
	
	
	
	public Comparison(List<Expression> exps, List<Character> oprs) {
		if (oprs.size() != exps.size()-1)
			throw new IllegalArgumentException("Invalid comparison!");
		expressions = exps;
		operators = oprs;
	}
	
	
	
	@Override
	public void execute() {
		return;
	}
	
	
	@Override
	public Statement simplified(HashMap<String, Expression> arg0) {
		boolean value = true;
		for (int i = 0; i < operators.size(); i ++) {
			//TODO: compare expressions
		}
		return new TrueFalse(value);
	}
	
	
	@Override
	public Image toImage() {
		Image img = expressions.get(0).toImage();
		for (int i = 0; i < operators.size(); i ++) {
			img = ImgUtils.horzCat(img,
					ImgUtils.drawString(" "+operators.get(i)+" ", false),
					expressions.get(i+1).toImage());
		}
		return img;
	}
	
	
	@Override
	public String toString() {
		String output = expressions.get(0).toString();
		for (int i = 0; i < operators.size(); i ++) {
			output += " "+operators.get(i)+" ";
			output += expressions.get(i+1).toString();
		}
		return output;
	}

}
