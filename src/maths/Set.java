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

import java.util.LinkedList;
import java.util.List;

import gui.Workspace;
import javafx.scene.image.Image;
import util.ImgUtils;

/**
 * A discrete list of Expressions
 * 
 * @author jkunimune
 */
public class Set extends Expression {

	public static final Set EMPTY = new Set(new LinkedList<Expression>());
	
	private final Expression[] elements;
	
	
	
	public Set(Expression...expressions) {
		elements = expressions;
	}
	
	
	public Set(List<Expression> exps) {
		elements = exps.toArray(new Expression[0]); //TODO: somewhere I should probably check to make sure the sizes match
	}
	
	
	
	@Override
	public int[] shape() {
		if (elements.length==0)
			return null;
		else
			return elements[0].shape();
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		return new Set(super.getComponentAll(elements, i,j));
	}
	
	
	@Override
	public List<String> getInputs(Workspace heap) {
		return super.getInputsAll(elements, heap);
	}
	
	
	@Override
	public Expression replaced(String[] oldStrs, String[] newStrs) {
		return new Set(super.replaceAll(elements, oldStrs, newStrs));
	}
	
	
	@Override
	public Expression simplified(Workspace heap) throws ArithmeticException { //TODO: maybe I should consider removing duplicates
		return new Set(super.simplifyAll(elements, heap));
	}
	
	
	@Override
	public Image toImage() {
		if (elements.length==0)	return ImgUtils.drawString("{}");
		
		List<Image> imgs = new LinkedList<Image>();
		for (Expression elm: elements)
			imgs.add(elm.toImage());
		return ImgUtils.wrap("{", ImgUtils.link(imgs, ","), "}");
	}
	
	
	@Override
	public String toString() {
		if (elements.length==0)	return "{}";
		String output = "{";
		for (Expression elm: elements)
			output += elm+", ";
		return output.substring(0,output.length()-2)+"}";
	}

}
