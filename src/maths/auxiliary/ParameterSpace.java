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
package maths.auxiliary;

import java.util.Iterator;

import javafx.scene.chart.NumberAxis;
import maths.Constant;

/**
 * A class for iterating over unknown variables
 * 
 * @author jkunimune
 */
public class ParameterSpace implements Iterable<Constant> {

	private final double min, max, step;
	
	
	
	private ParameterSpace(double min, double max, double step) {
		this.min = min;
		this.max = max;
		this.step = step;
	}
	
	
	
	public static ParameterSpace iterate(NumberAxis ax) {
		return iterate(
				1.5*ax.getLowerBound()-0.5*ax.getUpperBound(),
				1.5*ax.getUpperBound()-0.5*ax.getLowerBound());
	}
	
	
	public static ParameterSpace iterate(double min, double max) {
		return iterate(min, max, (max-min)/160);
	}
	
	
	public static ParameterSpace iterate(double min, double max, double step) {
		return new ParameterSpace(min, max, step);
	}
	
	
	@Override
	public Iterator<Constant> iterator() {
		return new Iterator<Constant>() {
			double val = min;
			
			@Override
			public boolean hasNext() {
				return val+step <= max;
			}
			
			@Override
			public Constant next() {
				val += step;
				return new Constant(val);
			}
			
		};
	}
	
}
