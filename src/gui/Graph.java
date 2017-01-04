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
import plots.Line2Plot;
import plots.Plot;
import plots.Scatter2Plot;

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
		try {
		
		List<String> independents = exp.getInputs(workspace); // count the inputs
		exp = exp.simplified(workspace);
		int dims = exp.shape()[0]*exp.shape()[1]; // and outputs
		
		Expression[] functions = null;
		if (independents.size() == 0) { // and determine what kind of graph to use
			if (dims == 1) {
				assert exp instanceof Constant: "How does a 1d simple exp with no inputs not be constant?";
				plot = new Scatter2Plot(PREF_WIDTH, PREF_HEIGHT);
				functions = new Expression[2];
				functions[0] = ((Constant) exp).re();
				functions[1] = ((Constant) exp).im();
			}
			else if (dims == 2) {
				plot = new Scatter2Plot(PREF_WIDTH, PREF_HEIGHT);
				functions = new Expression[2];
				functions[0] = exp.get(0);
				functions[1] = exp.get(1);
			}
			else if (dims == 3) {
				System.err.println("Single plot3 not implemented yet");
			}
		}
		else if (independents.size() == 1) {
			if (dims == 1) {
				plot = new Line2Plot(PREF_WIDTH, PREF_HEIGHT);
				functions = new Expression[2];
				functions[0] = new Variable(independents.get(0));
				functions[1] = exp;
			}
			else if (dims == 2) {
				plot = new Line2Plot(PREF_WIDTH, PREF_HEIGHT);
				functions = new Expression[2];
				functions[0] = exp.get(0);
				functions[1] = exp.get(1);
			}
			else if (dims == 3) {
				System.err.println("Path in 3space not implemented yet");
			}
		}
		else if (independents.size() == 2) {
			if (dims == 1) {
				System.err.println("Surface not implemented yet");
			}
			else if (dims == 2) {
				System.err.println("Quiver in 2space not implemented yet");
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
		
		} catch (NullPointerException e) {
			System.err.println("Still got some kinks to work out: "+e);
		} catch (AssertionError e) {
			System.err.println("Still got some kinks to work out: "+e);
		}
	}
	
	
	public void addPlot(Expression exp) {
		//TODO: Implement later
	}
	
	
	public Node getNode() {
		return pane;
	}

}
