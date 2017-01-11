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
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import gui.Workspace;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import maths.Expression;


/**
 * A three-dimensional interpolated line plot
 * 
 * @author jkunimune
 */
public class Line3Plot implements Plot {

	private final AWTChart chart;
	private final ImageView viewer;
	
	
	
	public Line3Plot(int w, int h) {
		JavaFXChartFactory factory = new JavaFXChartFactory();
		chart  = getDemoChart(factory, "offscreen");
		viewer = factory.bindImageView(chart);
		setSize(w, h);
	}
	
	
	
	@Override
	public Node getNode() {
		return viewer;
	}
	
	
	@Override
	public void setSize(int w, int h) {
		viewer.setFitWidth(w);
		viewer.setFitHeight(h);
	}
	
	
	@Override
	public void plot(Expression[] f, List<String> independent, Workspace heap) {
		// TODO Auto-generated method stub
		return;
	}
	
	
	private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit) {
		// -------------------------------
		// Define a function to plot
		Mapper mapper = new Mapper() {
			
			@Override
			public double f(double x, double y) {
				return x * Math.sin(x * y);
			}
		};
		
		// Define range and precision for the function to plot
		Range range = new Range(-3, 3);
		int steps = 80;
		
		// Create the object to represent the function over the given range.
		final Shape surface = Builder.buildOrthonormal(mapper, range, steps);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(),
				surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(false);
		
		// -------------------------------
		// Create a chart
		Quality quality = Quality.Advanced;
		// quality.setSmoothPolygon(true);
		// quality.setAnimated(true);
		
		// let factory bind mouse and keyboard controllers to JavaFX node
		AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
		chart.getScene().getGraph().add(surface);
		return chart;
	}

}
