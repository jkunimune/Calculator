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
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import gui.Workspace;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import maths.Constant;
import maths.Expression;


/**
 * A three-dimensional surface plot that only plots functions
 * 
 * @author jkunimune
 */
public class HeightPlot implements Plot {

	private IAxeLayout axes;
	private BoundingBox3d bounds;
	private AWTChart chart;
	private ImageView viewer;
	private final StackPane pane;
	
	private Shape prevSurface;
	
	
	
	public HeightPlot(int w, int h) {
		bounds = new BoundingBox3d(-4,4, -4,4, -4,4);
		chart = (AWTChart) new JavaFXChartFactory().newChart(Quality.Intermediate, "offscreen");
		pane = new StackPane();
		setSize(w, h);
		
		prevSurface = null;
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
	public void plot(Expression[] fnc, List<String> independent, Workspace heap) {
		if (prevSurface != null)	chart.getScene().getGraph().remove(prevSurface);
		assert independent.size() == 2 : "Illegal number of parameters";
		assert fnc.length == 1 : "Illegal number of dimensions";
		
		Workspace locHeap = heap.clone();
		Mapper mapper = new Mapper() {
			public double f(double x, double y) {
				locHeap.put(independent.get(0), new Constant(x));
				locHeap.put(independent.get(1), new Constant(y));
				return ((Constant) fnc[0].simplified(locHeap)).getReal();
			}
		};
		
		// Define range and precision for the function to plot
		Range range = new Range(-4, 4);
		int steps = 40;
		
		// Create the object to represent the function over the given range.
		final Shape surface = Builder.buildOrthonormal(mapper, range, steps);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(),
				Math.max(-4, surface.getBounds().getZmin()),
				Math.min(4, surface.getBounds().getZmax()),
				new Color(1, 1, 1, .5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(false);
		
		// let factory bind mouse and keyboard controllers to JavaFX node
		chart.getScene().getGraph().add(surface);
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
		
		prevSurface = surface;
	}

}
