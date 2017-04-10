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
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import gui.Workspace;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import maths.Constant;
import maths.Expression;
import maths.auxiliary.ParameterSpace;


/**
 * A three-dimensional interpolated line plot
 * 
 * @author jkunimune
 */
public class Line3Plot implements Plot {

	private IAxeLayout axes;
	private BoundingBox3d bounds;
	private AWTChart chart;
	private ImageView viewer;
	private final StackPane pane;
	
	private LineStrip prevCurve;
	
	
	
	public Line3Plot(int w, int h) {
		bounds = new BoundingBox3d(-4,4, -4,4, -4,4);
		chart = (AWTChart) new JavaFXChartFactory().newChart(Quality.Intermediate, "offscreen");
		pane = new StackPane();
		setSize(w, h);
		prevCurve = null;
	}
	
	
	
	@Override
	public Node getNode() {
		return pane;
	}
	
	
	@Override
	public void setSize(int w, int h) {
		pane.setPrefSize(w, h);
	}
	
	
	@Override
	public void plot(Expression[] f, List<String> independent, Workspace heap) {
		if (prevCurve != null)	chart.getScene().getGraph().remove(prevCurve);
		assert independent.size() == 1 : "Illegal number of parameters";
		assert f.length == 3 : "Illegal number of dimensions";
		
		LineStrip curve = new LineStrip();
		Workspace locHeap = heap.clone();
		for (Constant t: ParameterSpace.iterate(-6, 6)) {
			locHeap.put(independent.get(0), t);
			final double x = ((Constant) f[0].simplified(locHeap)).getReal();
			final double y = ((Constant) f[1].simplified(locHeap)).getReal();
			final double z = ((Constant) f[2].simplified(locHeap)).getReal();
			curve.add(new Point(new Coord3d(x, y, z), Color.RED));
		}
		curve.setWidth(4);
		// let factory bind mouse and keyboard controllers to JavaFX node
		chart.getScene().getGraph().add(curve);
		chart.getView().setBoundManual(bounds);
		
		axes = chart.getAxeLayout();
		axes.setXAxeLabel("");
		axes.setYAxeLabel("");
		axes.setZAxeLabel("");
		viewer = new JavaFXChartFactory().bindImageView(chart);
		viewer.setFitWidth(pane.getPrefWidth());
		viewer.setFitHeight(pane.getPrefHeight());
		pane.getChildren().clear();
		pane.getChildren().add(viewer);
		
		prevCurve = curve;
	}

}
