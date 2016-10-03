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
import maths.Notation;
import maths.Variable;

/**
 * The set of Nodes that manages basic user input and memory.
 *
 * @author jkunimune
 */
public class CommandLine {

	public static final int PREF_WIDTH = 400;
	
	private TextArea history;
	private TextField cmdLine;
	private ImageView displaySpace;
	
	private VBox container;
	
	private Workspace workspace;
	private Expression currentMath;
	
	
	
	public CommandLine(Workspace ws) {
		container = new VBox();
		container.setPrefWidth(PREF_WIDTH);
		
		history = new TextArea();
		history.setEditable(false);
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
		
		workspace = ws;
		currentMath = Expression.NULL;
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
		Expression math = currentMath;
		cmdLine.clear();
		history.appendText("\n"+text);	// write the current line to history
		
		if (text.isEmpty())	return;
		
		int i;
		if ((i = text.indexOf("=")) >= 0) {	// if there is an = operator
			math = Notation.parseExpression(text.substring(i+1));	// assign a new variable
			Expression left = Notation.parseExpression(text.substring(0,i));
			if (left instanceof Variable)
				workspace.put(left.toString(), math);
			else
				history.appendText("\nERR: Left side of = must be a variable!");	// unless error TODO: this should eventually do boolean stuff
			return;
		}
		
		final Expression ans = math.simplified(workspace.getHash());	// evaluate the expression
		history.appendText("\n\t= "+ans.toString());	// write the answer
		displaySpace.setImage(ans.toImage());
	}
	
	
	private void update(String input) {	// called when something is typed
		try {
			currentMath = Notation.parseExpression(input);
		} catch (IllegalArgumentException e) {
			System.err.println("Could not parse '"+input+"': "+e);
		}
		
		displaySpace.setImage(currentMath.toImage());
	}

}
