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
	
	private List<Expression> elements;
	
	
	
	public Set(List<Expression> exps) {
		elements = exps; //TODO: somewhere I should probably check to make sure the sizes match
	}
	
	
	
	@Override
	public int[] shape() {
		return elements.get(0).shape();
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		List<Expression> elementij = new ArrayList<Expression>(elements.size());
		for (Expression e: elements)
			elementij.add(e.getComponent(i, j));
		return new Set(elementij);
	}
	
	
	@Override
	public List<String> getInputs(Workspace heap) {
		List<String> inputs = new ArrayList<String>(); //TODO this could be a Collection
		for (Expression elm: elements)
			for (String newInput: elm.getInputs(heap))
				if (!inputs.contains(newInput))
					inputs.add(newInput);
		return inputs;
	}
	
	
	@Override
	public Expression replaced(List<String> oldStrs, List<String> newStrs) {
		List<Expression> modElms = new ArrayList<Expression>();
		for (Expression elm: elements)
			modElms.add(elm.replaced(oldStrs, newStrs));
		return new Set(modElms);
	}
	
	
	@Override
	public Expression simplified(Workspace heap) throws ArithmeticException { //TODO: maybe I should consider removing duplicates
		List<Expression> smpElms = new ArrayList<Expression>();
		for (Expression elm: elements)
			smpElms.add(elm.simplified(heap));
		return new Set(smpElms);
	}
	
	
	@Override
	public Image toImage() {
		List<Image> imgs = new LinkedList<Image>();
		for (Expression elm: elements)
			imgs.add(elm.toImage());
		return ImgUtils.wrap("{", ImgUtils.link(imgs, ","), "}");
	}
	
	
	@Override
	public String toString() {
		if (elements.isEmpty())	return "{}";
		String output = "{";
		for (Expression elm: elements)
			output += elm+", ";
		return output.substring(0,output.length()-2)+"}";
	}

}
