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

import java.util.Arrays;
import java.util.List;

import gui.Workspace;
import javafx.scene.image.Image;
import maths.Expression;
import util.ImgUtils;

/**
 * A set defined by an expression, containing an infinite number of points
 * 
 * @author jkunimune
 */
public class Locus extends Expression {

	private final Expression template;
	
	private final String[] params;
	private final Expression[] lowBounds, uppBounds;
	
	
	
	public Locus(Expression exp, String[] names,
			Expression[] low, Expression[] upp) {
		template = exp;
		params = names;
		lowBounds = low;
		uppBounds = upp;
	}
	
	
	public Locus(Expression exp, Expression low, Expression upp) {
		template = exp;
		params = new String[1];
		params[0] = exp.toString();
		lowBounds = new Expression[1];
		lowBounds[0] = low;
		uppBounds = new Expression[1];
		uppBounds[0] = upp;
	}
	
	
	
	@Override
	public int[] shape() {
		return template.shape();
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		return new Locus(template.getComponent(i, j),
				params, lowBounds, uppBounds);
	}
	
	
	@Override
	public List<String> getInputs(Workspace heap) {
		List<String> inputs = template.getInputs(heap);
		for (String varName: params)
			inputs.remove(varName);
		return inputs;
	}
	
	
	@Override
	public Expression replaced(String[] oldStrs, String[] newStrs) {
		String[] newParams = params.clone();
		for (int i = 0; i < newParams.length; i ++)
			for (int j = 0; j < oldStrs.length; j ++)
				if (newParams[i].equals(oldStrs[i]))
					newParams[i] = newStrs[i];
		return new Locus(template.replaced(oldStrs, newStrs), newParams,
				lowBounds, uppBounds);
	}
	
	
	@Override
	public Expression simplified(Workspace heap) throws ArithmeticException {
		final Workspace locHeap = heap.localize(Arrays.asList(params));
		return new Locus(template.simplified(locHeap), params,
				lowBounds, uppBounds);
	}
	
	
	@Override
	public Image toImage() {
		Image output =
				ImgUtils.horzCat(template.toImage(), ImgUtils.drawString(" | "));
		for (int i = 0; i < params.length; i ++) {
			Image low = lowBounds[i].toImage();
			Image name = ImgUtils.drawString(params[i], true);
			Image high = uppBounds[i].toImage();
			Image link = ImgUtils.drawString(" \u2264 ");
			output = ImgUtils.horzCat(output, low, link, name, link, high);
			if (i < params.length-1)
				output = ImgUtils.horzCat(output, ImgUtils.drawString(", "));
		}
		return ImgUtils.wrap("{", output, "}");
	}
	
	
	@Override
	public String toString() {
		String output = template.toString()+" | ";
		for (int i = 0; i < params.length; i ++) {
			String low = lowBounds[i].toString();
			String name = "\u2264"+params[i]+"\u2264"; // TODO differentiate between inclusive and exclusive
			String high = uppBounds[i].toString();
			output += low+name+high+", ";
		}
		return "{"+output.substring(0, output.length()-2)+"}";
	}

}
