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
package gui;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import maths.Constant;
import maths.Expression;
import maths.Variable;
import plots.HeightPlot;
import plots.Line2Plot;
import plots.Line3Plot;
import plots.Plot;
import plots.Scatter2Plot;
import plots.Scatter3Plot;
import plots.SurfacePlot;

/**
 * 
 * 
 * @author jkunimune
 */
public class Graph {

	public static final int PREF_WIDTH = 300;
	public static final int PREF_HEIGHT = 300;
	public static final double DEF_MIN_AXIS = -4;
	public static final double DEF_MAX_AXIS = 4;
	
	
	private StackPane pane;
	private Plot plot;
	
	private Workspace workspace;
	
	
	
	public Graph(Workspace ws) {
		plot = new Line2Plot(PREF_WIDTH, PREF_HEIGHT);
		pane = new StackPane(plot.getNode());
		workspace = ws;
	}
	
	
	
	public void setPlot(Expression exp) {
		List<String> independents = exp.getInputs(workspace); // count the inputs
		exp = exp.simplified(workspace);
		int dims = exp.shape()[0]*exp.shape()[1]; // and outputs
		
		Expression[] functions = null;
		if (independents.size() == 0) { // and determine what kind of graph to use
			if (dims == 1) {
				assert exp instanceof Constant: "How does a 1d simple exp with no inputs not be constant?";
				setPlotType(Scatter2Plot.class);
				functions = new Expression[2];
				functions[0] = ((Constant) exp).re();
				functions[1] = ((Constant) exp).im();
			}
			else if (dims == 2) {
				setPlotType(Scatter2Plot.class);
				functions = exp.getAll(2);
			}
			else if (dims == 3) {
				setPlotType(Scatter3Plot.class);
				functions = exp.getAll(3);
			}
		}
		else if (independents.size() == 1) {
			if (dims == 1) {
				setPlotType(Line2Plot.class); //FIXME: what?!
				functions = new Expression[2];
				functions[0] = new Variable(independents.get(0));
				functions[1] = exp;
			}
			else if (dims == 2) {
				setPlotType(Line2Plot.class);
				functions = exp.getAll(2);
			}
			else if (dims == 3) {
				setPlotType(Line3Plot.class);
				functions = exp.getAll(3);
			}
		}
		else if (independents.size() == 2) {
			if (dims == 1) {
				setPlotType(HeightPlot.class);
				functions = exp.getAll(1);
			}
			else if (dims == 2) {
				//System.err.println("Quiver in 2space not implemented yet");
				setPlotType(Line2Plot.class);
				functions = exp.getAll(2);
			}
			else if (dims == 3) {
				System.err.println("Parametric Surface not implemented yet");
			}
		}
		else if (independents.size() == 3) {
			if (dims == 1) {
				System.err.println("Bubble3 not implemented yet");
			}
			else if (dims == 2) {
				System.err.println("Colorful bubble3 not implemented yet");
			}
			else if (dims == 3) {
				System.err.println("Quiver in 3space not implemented yet");
			}
		}
		if (functions != null) {
			pane.getChildren().set(0, plot.getNode());
			plot.plot(functions, independents, workspace);
		}
	}
	
	
	public void addPlot(Expression exp) {
		//TODO: Implement later
	}
	
	
	public Node getNode() {
		return pane;
	}
	
	
	private void setPlotType(Class<? extends Plot> clazz) {
		if (clazz.isInstance(plot))	return;
		
		if (clazz == Scatter2Plot.class)
			plot = new Scatter2Plot(PREF_WIDTH, PREF_HEIGHT);
		else if (clazz == Line2Plot.class)
			plot = new Line2Plot(PREF_WIDTH, PREF_HEIGHT);
		else if (clazz == Scatter3Plot.class)
			plot = new Scatter3Plot(PREF_WIDTH, PREF_HEIGHT);
		else if (clazz == Line3Plot.class)
			plot = new Line3Plot(PREF_WIDTH, PREF_HEIGHT);
		else if (clazz == SurfacePlot.class)
			plot = new SurfacePlot(PREF_WIDTH, PREF_HEIGHT);
		else if (clazz == HeightPlot.class)
			plot = new HeightPlot(PREF_WIDTH, PREF_HEIGHT);
		else
			throw new IllegalArgumentException("What is "+clazz+" doing in this method?");
	}

}
