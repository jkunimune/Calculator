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

import gui.Workspace;
import javafx.scene.image.Image;
import util.ImgUtils;

/**
 * @author jkunimune
 */
public class TrueFalse implements Statement {

	private final boolean value;
	
	
	
	public TrueFalse(boolean b) {
		value = b;
	}
	
	
	
	@Override
	public Statement simplified(Workspace arg0) {
		return this;
	}
	
	
	@Override
	public Image toImage() {
		String out;
		if (value)	out = "\u2713";
		else		out = "\u2717";
		return ImgUtils.drawString(out, false);
	}
	
	
	@Override
	public String toString() {
		if (value)	return "True";
		else		return "False";
	}

}
