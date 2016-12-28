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
package plots;

import gui.Workspace;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import maths.Constant;
import maths.Expression;


/**
 * A two-dimension interpolated line plot
 * 
 * @author jkunimune
 */
public class Line2Plot implements Plot {

	private LineChart<Number, Number> chart;
	private NumberAxis xAxis, yAxis;
	
	
	
	public Line2Plot(int w, int h) {
		xAxis = new NumberAxis(-4, 4, 1);
		yAxis = new NumberAxis(-4, 4, 1);
		chart = new LineChart<Number, Number>(xAxis, yAxis);
		chart.setCreateSymbols(false);
		chart.setLegendVisible(false);
		setSize(w, h);
	}
	
	
	
	@Override
	public void plot(Expression f, Workspace heap) {
		assert f.getInputs(heap).size() == 1;
		f = f.simplified(heap);
		String independent = f.getInputs(heap).get(0);
		
		Workspace locHeap = heap.clone();
		XYChart.Series<Number, Number> data = new XYChart.Series<Number, Number>();
		for (double x = xAxis.getLowerBound(); x <= xAxis.getUpperBound(); x += (xAxis.getUpperBound()-xAxis.getLowerBound())/80.) { //XXX Set should be iterable, and there should be an iterable for R
			Constant z = new Constant(x);
			locHeap.put(independent, z);
			Constant u = (Constant) f.simplified(locHeap);
			//if (u.getImag() != 0)	continue; // skip numbers with imaginary components
			double y = u.getReal();
			data.getData().add(new XYChart.Data<Number, Number>(x,y));
		}
		
		chart.getData().clear();
		chart.getData().add(data);
	}
	
	
	@Override
	public Node getNode() {
		return chart;
	}
	
	
	@Override
	public void setSize(int w, int h) {
		chart.setPrefWidth(w);
		chart.setPrefHeight(h);
	}

}
