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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import maths.Expression;
import maths.Statement;
import maths.auxiliary.Notation;

/**
 * The set of Nodes that manages basic user input and memory.
 *
 * @author jkunimune
 */
public class CommandLine {

	public static final int PREF_WIDTH = 400;
	public static final int PREF_HEIGHT = 400;
	
	private TextArea history;
	private TextField cmdLine;
	private ImageView displaySpace;
	
	private VBox container;
	
	private Graph graph;
	private Workspace workspace;
	private Statement currentMath;
	private String errorMsg;
	
	
	
	public CommandLine(Graph gr, Workspace ws) {
		container = new VBox();
		container.setPrefWidth(PREF_WIDTH);
		
		history = new TextArea();
		history.setEditable(false);
		history.setPrefHeight(PREF_HEIGHT);
		container.getChildren().add(history);
		
		cmdLine = new TextField();
		cmdLine.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				evaluate();
			}
		});
		cmdLine.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				update(newValue);
			}
		});
		container.getChildren().add(cmdLine);
		
		container.getChildren().add(new Separator());
		
		displaySpace = new ImageView();
		container.getChildren().add(displaySpace);
		
		graph = gr;
		workspace = ws;
		currentMath = Expression.NULL;
		errorMsg = "";
	}
	
	
	
	public Node getNode() {	// get all components in JavaFX format
		return container;
	}
	
	
	public void typeText(String text) {	// add text to the command line
		cmdLine.appendText(text);
	}
	
	
	public void requestFocus() {
		cmdLine.requestFocus();
		cmdLine.positionCaret(cmdLine.getLength());
	}
	
	
	private void evaluate() {	// called when enter is pressed
		final String text = cmdLine.getText();
		Statement math = currentMath; // save the math and the message
		String errMsg = errorMsg;
		cmdLine.clear();
		history.appendText("\n"+text);	// write the current line to history
		
		if (text.isEmpty())	return;
		
		if (!errMsg.isEmpty()) {
			history.appendText("\nSYNTAX ERROR: "+errMsg);
			return;
		}
		
		try {
			final Statement ans = math.simplified(workspace);	// evaluate the expression
			if (ans != null) {
				if (ans instanceof Expression) {
					history.appendText("\n\t= "+ans.toString());	// write the answer
					graph.setPlot((Expression) ans);
				}
				else
					history.appendText("\n\t"+ans.toString());
				displaySpace.setImage(ans.toImage());
			}
		} catch (ArithmeticException e) {
			history.appendText("\nERROR: "+e.getMessage());	// print the error if there is one
		} catch (IllegalArgumentException e) {
			history.appendText("\nMEMORY ERROR: "+e.getMessage());
		}
	}
	
	
	private void update(String input) {	// called when something is typed
		try {
			currentMath = Notation.parseStatement(input);
			errorMsg = "";
		} catch (IllegalArgumentException e) {
			currentMath = Expression.ERROR;
			errorMsg = e.getMessage();
		}
		
		displaySpace.setImage(currentMath.toImage());
	}

}
