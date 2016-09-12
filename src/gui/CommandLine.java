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

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * The set of Nodes that manages basic user input and memory.
 *
 * @author jkunimune
 */
public class CommandLine {

	private TextArea history;
	private TextField cmdLine;
	
	private VBox container;
	
	
	
	public CommandLine() {
		container = new VBox();
		
		history = new TextArea();
		history.setEditable(false);
		history.setText("\n\n\n\n\n\n\n\n2+2=\n         4");
		container.getChildren().add(history);
		
		cmdLine = new TextField();
		container.getChildren().add(cmdLine);
		cmdLine.requestFocus();
		
		Canvas disp = new Canvas(500, 50);
		GraphicsContext g = disp.getGraphicsContext2D();
		g.fillText("Hello, Worcestor!", 5, 50);
		g.fillText("Hello, Worcestor!", 15, 50);
		g.fillText("Hello, Worcestor!", 5, 40);
		g.fillText("Hello, Worcestor!", 15, 40);
		g.lineTo(420, 42);
		container.getChildren().add(disp);
	}
	
	
	
	public Node getNode() {	// get all components in JavaFX format
		return container;
	}

}
