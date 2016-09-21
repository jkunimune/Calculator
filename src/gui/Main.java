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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The main class, which starts the application and assembles the GUI.
 *
 * @author jkunimune
 */
public class Main extends Application {

	public static final void main(String[] args) {
		launch(args);
	}
	
	
	
	private OperationBar toolbar;
	private CommandLine cmdLine;
	private Workspace workspace;
	
	
	
	@Override
	public void start(Stage primaryStage) {
		Pane root = new StackPane();
		Scene scene = new Scene(root);
		
		HBox layout = new HBox();
		root.getChildren().add(layout);
		
		workspace = new Workspace();
		cmdLine = new CommandLine(workspace);
		toolbar = new OperationBar(cmdLine);
		layout.getChildren().add(toolbar.getNode());
		layout.getChildren().add(cmdLine.getNode());
		layout.getChildren().add(workspace.getNode());
		
		cmdLine.requestFocus();
		
		primaryStage.setTitle("Math-Assist");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
