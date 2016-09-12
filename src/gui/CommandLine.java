/**
 * The set of Nodes that manages basic user input and memory.
 */
package gui;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
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
