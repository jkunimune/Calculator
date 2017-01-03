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
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import maths.Constant;
import maths.Expression;


/**
 * A two-dimension interpolated line plot
 * 
 * @author jkunimune
 */
public class Scatter2Plot implements Plot {

	private ScatterChart<Number, Number> chart;
	private NumberAxis xAxis, yAxis;
	
	
	
	public Scatter2Plot(int w, int h) {
		xAxis = new NumberAxis(-4, 4, 1);
		yAxis = new NumberAxis(-4, 4, 1);
		chart = new ScatterChart<Number, Number>(xAxis, yAxis);
		chart.setLegendVisible(false);
		setSize(w, h);
	}
	
	
	
	@Override
	public void plot(Expression[] f, List<String> params,
			Workspace heap) {
		assert f.length == 2 : "Illegal number of dimensions";
		assert params.isEmpty(): "You can't scatter a curve";
		Constant fx = (Constant) f[0];
		Constant fy = (Constant) f[1];
		
		XYChart.Series<Number, Number> data = new XYChart.Series<Number, Number>();
		data.getData().add(new XYChart.Data<Number, Number>(fx.getReal(),fy.getReal()));
		
		chart.getData().clear();
		chart.getData().add(data);
		autoScale(data);
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
	
	
	private void autoScale(XYChart.Series<Number, Number> data) {
		double xMin=-3, xMax=3, yMin=-3, yMax=3;
		for (XYChart.Data<Number, Number> datum: data.getData()) { // get the limits of the data
			if (datum.getXValue().doubleValue() > xMax)
				xMax = datum.getXValue().doubleValue();
			
			else if (datum.getXValue().doubleValue() < xMin)
				xMin = datum.getXValue().doubleValue();
			
			if (datum.getYValue().doubleValue() > yMax)
				yMax = datum.getYValue().doubleValue();
			
			else if (datum.getYValue().doubleValue() < yMin)
				yMin = datum.getYValue().doubleValue();
		}
		
		double xTick, yTick; // set the axes appropriately
		double xOrder = Math.log10(xMax-xMin);
		xTick = Math.pow(10, (int)(xOrder));
		if (xOrder%1 < 0.5)
			xTick /= 2;
		xAxis.setUpperBound((Math.floor(xMax/xTick)+1)*xTick);
		xAxis.setLowerBound((Math.ceil(xMin/xTick)-1)*xTick);
		xAxis.setTickUnit(xTick);
		
		double yOrder = Math.log10(yMax-yMin)-0.1;
		yTick = Math.pow(10, (int)(yOrder));
		if (yOrder%1 < 0.5)
			yTick /= 2;
		yAxis.setUpperBound((Math.floor(yMax/yTick)+1)*yTick);
		yAxis.setLowerBound((Math.ceil(yMin/yTick)-1)*yTick);
		yAxis.setTickUnit(yTick);
	}

}
