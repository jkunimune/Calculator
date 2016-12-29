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

import java.util.List;

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
	public void plot(Expression[] f, List<String> params,
			Workspace heap) {
		assert f.length == 2 : "Illegal number of dimensions";
		Expression fx = f[0];
		Expression fy = f[1];
		
		assert params.size() == 1 : "I haven't implemented meshes yet";
		//fx = fx.simplified(heap);
		//fy = fy.simplified(heap);
		
		Workspace locHeap = heap.clone();
		XYChart.Series<Number, Number> data = new XYChart.Series<Number, Number>();
		for (double t = xAxis.getLowerBound(); t <= xAxis.getUpperBound(); t += (xAxis.getUpperBound()-xAxis.getLowerBound())/80.) { //XXX Set should be iterable, and there should be an iterable for R
			Constant input = new Constant(t);
			locHeap.put(params.get(0), input);
			Constant x = (Constant) fx.simplified(locHeap);
			Constant y = (Constant) fy.simplified(locHeap);
			//if (u.getImag() != 0)	continue; // skip numbers with imaginary components
			data.getData().add(new XYChart.Data<Number, Number>(x.getReal(),y.getReal()));
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
