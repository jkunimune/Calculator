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
import java.util.Random;

import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import gui.Workspace;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import maths.Expression;


/**
 * A three-dimensional interpolated line plot
 * 
 * @author jkunimune
 */
public class Scatter3Plot implements Plot {

	private IAxeLayout axes;
	private AWTChart chart;
	private ImageView viewer;
	private final StackPane pane;
	
	
	
	public Scatter3Plot(int w, int h) {
		chart = (AWTChart) new JavaFXChartFactory().newChart(Quality.Intermediate, "offscreen");
		pane = new StackPane();
		setSize(w, h);
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
		plotDemogh();
		
		axes = chart.getAxeLayout();
		axes.setXAxeLabel("");
		axes.setYAxeLabel("");
		axes.setZAxeLabel("");
		viewer = new JavaFXChartFactory().bindImageView(chart);
		viewer.setFitWidth(pane.getPrefWidth());
		viewer.setFitHeight(pane.getPrefHeight());
		pane.getChildren().clear();
		pane.getChildren().add(viewer);
	}
	
	
	private void plotDemogh() {
		int size = 50;
        float x;
        float y;
        float z;
        
        Coord3d[] points = new Coord3d[size];
        Color[]   colors = new Color[size];
        
        Random r = new Random();
        r.setSeed(0);
        
        for(int i=0; i<size; i++){
            x = r.nextFloat() - 0.5f;
            y = r.nextFloat() - 0.5f;
            z = r.nextFloat() - 0.5f;
            points[i] = new Coord3d(x, y, z);
            colors[i] = Color.RED;
        }
        
        Scatter scatter = new Scatter(points, colors);
        scatter.setWidth(10);
        //chart = (AWTChart) AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);
	}

}
