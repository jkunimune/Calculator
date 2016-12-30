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

import gui.Workspace;
import javafx.scene.image.Image;
import util.ImgUtils;

/**
 * An expression whose value is unknown, but may be found in the heap.
 *
 * @author jkunimune
 */
public class Variable extends Expression {

	String name;
	
	
	
	public Variable(String s) {
		name = s;
	}
	
	
	
	@Override
	public int[] shape() {
		final int[] out = {1,1};
		return out;
	}
	
	
	@Override
	protected Expression getComponent(int i, int j) {
		return this; //TODO: variables can be vectors, too, right?
	}
	
	
	@Override
	public List<String> getInputs(Workspace heap) {
		if (heap.containsKey(name)){
			return heap.get(name).getInputs(heap);
		}
		else {
			List<String> list = new ArrayList<String>();
			list.add(name);
			return list;
		}
	}
	
	
	@Override
	public Expression replaced(List<String> oldS, List<String> newS) {
		for (int i = 0; i < oldS.size(); i ++)
			if (oldS.get(i).equals(name))
				return new Variable(newS.get(i));
		return this;
	}
	
	
	@Override
	public Expression simplified(Workspace heap) {
		if (heap != null && heap.containsKey(name))
			return heap.get(name).simplified(heap);
		else
			return this;
	}
	
	
	@Override
	public Image toImage() {
		return ImgUtils.horzCat(ImgUtils.drawString(name.substring(0, 1), true),
				ImgUtils.subS(ImgUtils.drawString(name.substring(1))));
	}
	
	
	@Override
	public String toString() {
		return name;
	}

}
