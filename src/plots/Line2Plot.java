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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gui.Workspace;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import maths.Constant;
import maths.Expression;
import maths.auxiliary.ParameterSpace;

/**
 * A two-dimension interpolated line plot
 * 
 * @author jkunimune
 */
public class Line2Plot implements Plot {

	public static final String[] COLORS = {"crimson","royalblue","forestgreen"};
	
	
	private final LineChart<Number, Number> chart;
	private NumberAxis xAxis, yAxis;
	
	
	
	public Line2Plot(int w, int h) {
		xAxis = new NumberAxis(-4, 4, 1);
		yAxis = new NumberAxis(-4, 4, 1);
		chart = new LineChart<Number, Number>(xAxis, yAxis);
		chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
		chart.setCreateSymbols(false);
		chart.setLegendVisible(false);
		setSize(w, h);
	}
	
	
	
	@Override
	public void plot(Expression[] f, List<String> params,
			Workspace heap) {
		clearChart();
		
		assert f.length == 2 : "Illegal number of dimensions";
		Expression fx = f[0];
		Expression fy = f[1];
		
		Workspace locHeap = heap.clone();
		
		List<Iterator<Constant>> paramChooser =
				new ArrayList<Iterator<Constant>>(params.size());
		ParameterSpace iteratorFactory = ParameterSpace.iterate(xAxis, 1);
		for (int i = 0; i < params.size(); i ++) {
			paramChooser.add(iteratorFactory.iterator());
			locHeap.put(params.get(i), paramChooser.get(i).next());
		}
		
		for (int varying = 0; varying < params.size(); varying ++) { // for each set of lines
			boolean firstCurve = true;
			for (int i = 0; !(firstCurve && i!= 0); i ++) {
				double[] deli = null, delf = null;
				XYChart.Series<Number, Number> data = new XYChart.Series<Number, Number>();
				for (Constant t: ParameterSpace.iterate(xAxis)) {
					locHeap.put(params.get(varying), t);
					Constant x = (Constant) fx.simplified(locHeap);
					Constant y = (Constant) fy.simplified(locHeap);
					//if (x.getImag() != 0 || y.getImag() != 0)	continue; // skip numbers with imaginary components
					XYChart.Data<Number, Number> p =
							new XYChart.Data<Number, Number>(x.getReal(),y.getReal());
					if (!data.getData().isEmpty()) {
						delf = new double[2];
						delf[0] = p.getXValue().doubleValue()-data.getData().get(data.getData().size()-1).getXValue().doubleValue();
						delf[1] = p.getYValue().doubleValue()-data.getData().get(data.getData().size()-1).getYValue().doubleValue();
					}
					if (deli != null && delf != null &&
							(deli[0]*delf[0]+deli[1]*delf[1] < 0 ||
							Math.hypot(delf[0],delf[1])/Math.hypot(deli[0],deli[1])>10)) { // change in direction or sudden acceleration implies discontinuity
						addToChart(data, COLORS[varying%COLORS.length]);
						data = new XYChart.Series<Number, Number>();
						delf = null;
					}
					data.getData().add(p);
					deli = delf;
				}
				addToChart(data, COLORS[varying%COLORS.length]);
				
				firstCurve = true;
				for (int j = 0; j < params.size(); j ++) { // hold the other parameters constant at some value
					if (j != varying) {
						if (paramChooser.get(j).hasNext()) { // find the first iterator that has a value and take that value
							locHeap.put(params.get(j), paramChooser.get(j).next());
							firstCurve = false;
							break;
						}
						else { // reset any before it that do not
							paramChooser.set(j, iteratorFactory.iterator());
							locHeap.put(params.get(j), paramChooser.get(j).next());
						}
					}
				}
			}
			paramChooser.set(varying, iteratorFactory.iterator());
			locHeap.put(params.get(varying), paramChooser.get(varying).next());
		}
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
	
	
	private void clearChart() {
		chart.getData().clear();
	}
	
	
	private void addToChart(Series<Number, Number> data, String color) {
		chart.getData().add(data);
		data.getNode().setStyle("-fx-stroke: "+color+";");
	}

}
